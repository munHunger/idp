package se.munhunger.idp.dao;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class DatabaseDAO {
    private static Logger logger = Logger.getLogger(DatabaseDAO.class.getName());
    protected static SessionFactory sessionFactory;

    private static void init()
    {
        logger.info("Initializing database");
        Map<String, Object> configMap = new HashMap<>();
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");
        if(url != null) {
            logger.info(() -> "DB_URL Environment was set, setting hibernate.connection.url to " + url);
            configMap.put("hibernate.connection.url", "jdbc:" + url);
        }
        if(user != null) {
            logger.info(() -> "DB_USER Environment was set, setting hibernate.connection.username to " + user);
            configMap.put("hibernate.connection.username", user);
        }
        if(pass != null) {
            logger.info(() -> "DB_PASS Environment was set, setting hibernate.connection.password");
            configMap.put("hibernate.connection.password", pass);
        }
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().applySettings(configMap)
                .build();
        Configuration config = new Configuration();


        sessionFactory = config.buildSessionFactory(registry);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> StandardServiceRegistryBuilder.destroy(registry)));
    }

    static {
        init();
    }
}