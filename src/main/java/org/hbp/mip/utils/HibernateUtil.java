/**
 * Created by mirco on 07.12.15.
 */

package org.hbp.mip.utils;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

    private static final Logger LOGGER = Logger.getLogger(CSVUtil.class);

    private static ServiceRegistry serviceRegistry;

    private static SessionFactory sessionFactory = buildSessionFactory();

    private HibernateUtil() {
        /* Hide implicit public constructor */
        throw new IllegalAccessError("HibernateUtil class");
    }

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                    configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            return sessionFactory;
        } catch (RuntimeException ex) {
            LOGGER.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
