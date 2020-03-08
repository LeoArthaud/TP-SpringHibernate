package persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionSingleton {
    public static SessionFactory sessionFactoryInstance = new Configuration().configure().buildSessionFactory();
    public static SessionFactory getSession(){return sessionFactoryInstance;}
}