package es.albarregas.dao;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class HibernateUtil {
    private static final SessionFactory SESSIONFACTORY;
    protected Session sesion;
    protected Transaction transaccion;
    
    static {
        try {
            SESSIONFACTORY = new Configuration().configure().buildSessionFactory();
        }
        catch (Throwable e) {
            System.err.println("[ERROR - es.albarregas.hibernate.HibernateUtil.java] Fallo en método creación\n" + e);
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public void openSession() {
        if(sesion != null && sesion.isOpen()) {
            closeSession();
        }
        
        sesion = SESSIONFACTORY.openSession();
    }
    
    public void closeSession() {
        if(sesion != null && sesion.isOpen()) {
            sesion.close();
        }
    }
}