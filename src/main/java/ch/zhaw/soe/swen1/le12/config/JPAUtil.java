package ch.zhaw.soe.swen1.le12.config;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Helper class to facilitate getting a ready to use EntityMangerFactory 
 * configured by an XML configuration file in META-INF.
 */
public class JPAUtil {
    private static final String PERSISTENCE_UNIT_NAME = "PERSISTENCE";
    private static EntityManagerFactory factory;

    /**
     * Returns the singleton EntityManagerFactory instance. 
     * @return
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return factory;
    }

    public static void shutdown() {
        if (factory != null) {
            factory.close();
        }
    }
}
