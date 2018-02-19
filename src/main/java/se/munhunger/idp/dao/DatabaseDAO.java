package se.munhunger.idp.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DatabaseDAO {
    private static Logger log = LogManager.getLogger(DatabaseDAO.class.getName());
    protected static SessionFactory sessionFactory;

    public static void resetSessions()
    {
        sessionFactory = null;
    }

    private static void init()
    {
        log.info(() -> "Opening Database");
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure()
                .build();
        MetadataSources mds = new MetadataSources(registry);
        Metadata md = mds.buildMetadata();
        sessionFactory = md.buildSessionFactory();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> StandardServiceRegistryBuilder.destroy(registry)));
        log.info(() -> "Opening Database Successful");
    }

    static {
        init();
    }
}
