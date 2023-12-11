package net.kettlemc.kcommon.data;

import net.kettlemc.kcommon.java.Provider;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * A data handler that uses Hibernate to save and load entities
 *
 * @param <T> the type of entity
 */
public class HibernateDataHandler<T> implements DataHandler<T> {

    protected final Class<?> type;
    protected final String sqlHost;
    protected final String sqlPort;
    protected final String sqlDatabase;
    protected final String sqlUser;
    protected final String sqlPassword;
    protected final Provider<T> defaultEntityProvider;
    protected SessionFactory sessionFactory;
    protected DataThreadHandler dataThreadHandler;
    private String tableNameFormat = "%s";

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
    public HibernateDataHandler(Class<?> type, Provider<T> defaultEntityProvider, String sqlHost, String sqlPort, String sqlDatabase, String sqlUser, String sqlPassword) {
        this.type = type;
        this.defaultEntityProvider = defaultEntityProvider;
        this.sqlHost = sqlHost;
        this.sqlPort = sqlPort;
        this.sqlDatabase = sqlDatabase;
        this.sqlUser = sqlUser;
        this.sqlPassword = sqlPassword;
    }

    public HibernateDataHandler<T> setTableNameFormat(String tableNameFormat) {
        this.tableNameFormat = tableNameFormat;
        return this;
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
            if (this.type.isAnnotationPresent(Table.class))
                return;

            String table = this.type.getAnnotation(Table.class).name();

            List<T> entities = session.createQuery("SELECT * FROM " + table + ";").list();
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
                entity = this.defaultEntityProvider.get();
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
                    .setProperty("hibernate.connection.url", "jdbc:mysql://" + this.sqlHost + ":" + this.sqlPort + "/" + this.sqlDatabase + "?useSSL=false")
                    .setProperty("hibernate.connection.username", this.sqlUser)
                    .setProperty("hibernate.connection.password", this.sqlPassword)
                    .setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect")
                    .setProperty("hibernate.show_sql", "false")
                    .setProperty("hibernate.hbm2ddl.auto", "update")
                    .setProperty("hibernate.connection.pool_size", "1")
                    .setProperty("hibernate.current_session_context_class", "thread")
                    .addAnnotatedClass(this.type);

            configuration.setImplicitNamingStrategy(DynamicNamingStrategy.of(this.tableNameFormat));

            this.sessionFactory = configuration.buildSessionFactory();

            this.dataThreadHandler = new DataThreadHandler();
            this.dataThreadHandler.init();
            return true;
        } catch (HibernateException | IllegalStateException throwable) {
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
