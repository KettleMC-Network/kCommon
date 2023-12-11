package net.kettlemc.kcommon.data;

import net.kettlemc.kcommon.java.Provider;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;

public class HibernateSQLiteDataHandler<T> extends HibernateDataHandler<T> {

    /**
     * Creates a new HibernateSQLiteDataHandler
     *
     * @param type                  the type of entity
     * @param defaultEntityProvider a provider for the default entity
     * @param database              the sql database
     */
    public HibernateSQLiteDataHandler(Class<?> type, Provider<T> defaultEntityProvider, String database) {
        super(type, defaultEntityProvider, null, null, database, null, null);
    }

    @Override
    public boolean initialize() {
        try {
            Configuration config = new Configuration()
                    .setProperty("hibernate.connection.url", "jdbc:sqlite:test.sqlite")
                    .setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC")
                    .setProperty("hibernate.dialect", "org.sqlite.hibernate.dialect.SQLiteDialect")
                    .setProperty("hibernate.show_sql", "false")
                    .setProperty("hibernate.hbm2ddl.auto", "update")
                    .setProperty("hibernate.connection.pool_size", "1")
                    .setProperty("hibernate.current_session_context_class", "thread")
                    .addAnnotatedClass(this.type);
            config.setImplicitNamingStrategy(DynamicNamingStrategy.of(""));
            this.sessionFactory = config.buildSessionFactory();

            this.dataThreadHandler = new DataThreadHandler();
            this.dataThreadHandler.init();
            return true;
        } catch (HibernateException | IllegalStateException throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

}
