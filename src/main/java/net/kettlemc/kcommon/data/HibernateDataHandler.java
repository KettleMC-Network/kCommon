package net.kettlemc.kcommon.data;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * A data handler that uses Hibernate to save and load entities
 *
 * @param <T> the type of entity
 */
public class HibernateDataHandler<T> implements DataHandler<T> {

    private static final Logger LOGGER = Logger.getLogger(HibernateDataHandler.class.getName());

    protected final Class<?> type;
    protected final String sqlHost;
    protected final String sqlPort;
    protected final String sqlDatabase;
    protected final String sqlUser;
    protected final String sqlPassword;
    protected final Function<String, T> defaultEntityProvider;
    protected SessionFactory sessionFactory;
    protected DataThreadHandler dataThreadHandler;

    /**
     * Creates a new HibernateDataHandler
     *
     * @param type                  the type of entity
     * @param defaultEntityProvider a provider for the default entity
     * @param sqlHost               the sql host
     * @param sqlPort               the sql port
     * @param sqlDatabase           the sql database
     * @param sqlUser               the sql user
     * @param sqlPassword           the sql password
     */
    public HibernateDataHandler(Class<T> type, Function<String, T> defaultEntityProvider, String sqlHost, String sqlPort, String sqlDatabase, String sqlUser, String sqlPassword) {
        this.type = type;
        this.defaultEntityProvider = defaultEntityProvider;
        this.sqlHost = sqlHost;
        this.sqlPort = sqlPort;
        this.sqlDatabase = sqlDatabase;
        this.sqlUser = sqlUser;
        this.sqlPassword = sqlPassword;

        if (!this.type.isAnnotationPresent(Entity.class)) {
            throw new IllegalArgumentException("Entity type must be annotated with @Entity");
        }
    }

    /**
     * Creates a new HibernateDataHandler
     *
     * @param type        the type of entity
     * @param sqlHost     the sql host
     * @param sqlPort     the sql port
     * @param sqlDatabase the sql database
     * @param sqlUser     the sql user
     * @param sqlPassword the sql password
     */
    public HibernateDataHandler(Class<T> type, String sqlHost, String sqlPort, String sqlDatabase, String sqlUser, String sqlPassword) {
        this(type, (id) -> null, sqlHost, sqlPort, sqlDatabase, sqlUser, sqlPassword);
    }

    @Override
    public Future<T> save(@NotNull T entity) {
        if (!initialized()) {
            throw new IllegalStateException("HibernateDataHandler not initialized!");
        }

        CompletableFuture<T> future = new CompletableFuture<>();
        this.dataThreadHandler.queue(() -> {
            Session session = this.sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(entity);
            session.getTransaction().commit();
            session.close();
            future.complete(entity);
        });
        return future;
    }

    @Override
    public Future<T> load(@NotNull String uuid) {
        return load(uuid, true);
    }

    public Future<List<T>> loadAll() {
        CompletableFuture<List<T>> future = new CompletableFuture<>();
        this.dataThreadHandler.queue(() -> {
            Session session = this.sessionFactory.openSession();
            session.beginTransaction();

            String table = this.type.getAnnotation(Entity.class).name();

            List<T> entities = session.createQuery("FROM " + table).list();
            if (entities == null || entities.isEmpty()) {
                entities = new ArrayList<>();
            }
            session.getTransaction().commit();
            session.close();
            future.complete(entities);
        });
        return future;
    }

    @Override
    public Future<T> load(@NotNull String uuid, boolean createIfNotExists) {
        CompletableFuture<T> future = new CompletableFuture<>();
        this.dataThreadHandler.queue(() -> {
            Session session = this.sessionFactory.openSession();
            session.beginTransaction();
            @SuppressWarnings("unchecked")
            T entity = (T) session.get(this.type, uuid);
            if (entity == null && createIfNotExists) {
                entity = this.defaultEntityProvider.apply(uuid);
                session.saveOrUpdate(entity);
            }
            session.getTransaction().commit();
            session.close();
            future.complete(entity);
        });
        return future;
    }

    @Override
    public boolean initialized() {
        return sessionFactory != null && sessionFactory.isOpen();
    }

    @Override
    public boolean initialize() {
        try {
            Configuration configuration = new Configuration()
                    .setProperty("hibernate.connection.url", "jdbc:mysql://" + this.sqlHost + ":" + this.sqlPort + "/" + this.sqlDatabase + "?useSSL=true")
                    .setProperty("hibernate.connection.username", this.sqlUser)
                    .setProperty("hibernate.connection.password", this.sqlPassword)
                    .setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect")
                    .setProperty("hibernate.show_sql", "false")
                    .setProperty("hibernate.hbm2ddl.auto", "update")
                    .setProperty("hibernate.connection.provider_class", "com.zaxxer.hikari.hibernate.HikariConnectionProvider")
                    .setProperty("hibernate.hikari.minimumIdle", "5")
                    .setProperty("hibernate.hikari.maximumPoolSize", "15")
                    .setProperty("hibernate.hikari.idleTimeout", "60000")
                    .setProperty("hibernate.hikari.connectionTimeout", "30000")
                    .setProperty("hibernate.hikari.maxLifetime", "1800000")
                    .setProperty("hibernate.hikari.leakDetectionThreshold", "5000")
                    .setProperty("hibernate.hikari.poolName", "HibernatePool-" + Math.abs(UUID.randomUUID().hashCode()))
                    .setProperty("hibernate.current_session_context_class", "thread")
                    .addAnnotatedClass(this.type);

            this.sessionFactory = configuration.buildSessionFactory();

            // Improved thread handler initialization to ensure it's not null in edge cases
            this.dataThreadHandler = new DataThreadHandler();
            this.dataThreadHandler.init();
            return true;
        } catch (HibernateException | IllegalStateException throwable) {
            LOGGER.severe("Initialization failed: " + throwable.getMessage());
            throwable.printStackTrace();
            return false;
        }
    }


    @Override
    public void close() {
        if (this.sessionFactory != null) {
            this.sessionFactory.close();
        }
        if (this.dataThreadHandler != null) {
            this.dataThreadHandler.shutdown();
        }
    }
}
