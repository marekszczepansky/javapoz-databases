package pl.szczepanski.marek.demo.databases;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

@Component
public class TestCode {

    private final SessionFactory hibernateFactory;

    public TestCode(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.hibernateFactory = factory.unwrap(SessionFactory.class);
    }

    @PostConstruct
    public void testHibernateCode() {
        System.out.printf("\n\n---------------------\nTest code started\n\n");

        Transaction tx = null;
        try (Session session = hibernateFactory.openSession()) {
            tx = session.beginTransaction();

//            Course c = new Course();
//
//            c.name = "code 1";
//            session.persist(c);
//
//            c = new Course();
//            c.name = "code 2";
//            session.persist(c);

            tx.commit();
        } catch (Exception ex) {
            if (tx != null && !tx.getRollbackOnly()) {
                tx.rollback();
            }
            throw ex;
        }



        System.out.printf("\n---------------------\nTest code completed\n\n");

    }
}
