package pl.szczepanski.marek.demo.databases;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import pl.szczepanski.marek.demo.databases.entities.Course;
import pl.szczepanski.marek.demo.databases.entities.Student;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.HashSet;

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

            Course c1 = new Course();

            c1.name = "course 1";
            c1.startDate = LocalDate.of(2019, 2, 26);
            session.persist(c1);

            Course c2 = new Course();
            c2.name = "course 2";
            c2.startDate = LocalDate.of(2019, 2, 20);
            session.persist(c2);
            session.evict(c2);

            tx.commit();

            tx = session.beginTransaction();

            Course c3 = session.find(Course.class, 1);
            System.out.printf("\nfound " + c3.name);
            c3.name = "course 1 updated";

            tx.commit();

            tx = session.beginTransaction();

            Course c4 = new Course();
            c4.name = "nowy stan obiektu po update";
            c4.startDate = LocalDate.of(2019, 2, 28);
            c4.id = 2;

            session.update(c4);

            tx.commit();

            tx = session.beginTransaction();

            Query<Course> courseQuery = session.createQuery(
                    "from Course where name like :nameparam",
                    Course.class);
            courseQuery.setParameter("nameparam", "course%");

            Course c5 = courseQuery.list().get(0);

            System.out.printf("\ncourse selected by query: " + c5.name);

            tx.commit();

            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(Course.class);
            criteria.add(Restrictions.eq("name", "course 1 updated"));
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            Course c6 = (Course) criteria.list().get(0);

            System.out.printf("\ncourse selected by criteria: " + c6.name);

            tx.commit();

            tx = session.beginTransaction();

            Student s1 = new Student();
            s1.name = "student 1";
            s1.address = "Poznan";
            s1.courses = new HashSet<>();
            s1.courses.add(c6);
            s1.courses.add(c4);
            session.persist(s1);

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
