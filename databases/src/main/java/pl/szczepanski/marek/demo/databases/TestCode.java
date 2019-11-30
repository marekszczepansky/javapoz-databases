package pl.szczepanski.marek.demo.databases;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import pl.szczepanski.marek.demo.databases.entities.Course;
import pl.szczepanski.marek.demo.databases.entities.Student;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.util.List;

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
        final int course2Id;

        Transaction tx = null;
        try (Session session = hibernateFactory.openSession()) {
            tx = session.beginTransaction();

            Course c;

            c = new Course();
            c.setName("code 1");
            session.persist(c);

            c = new Course();
            c.setName("code 2");
            session.persist(c);

            tx.commit();
            tx = session.beginTransaction();

            Student student = new Student();
            student.setName("Jan Smith");
            student.setAddress("2nd avenue 1234");
            student.setCourse(c);
            session.persist(student);

            Student student2 = new Student();
            student2.setName("Marek Test");
            student2.setAddress("1st street 123");
            student2.setCourse(c);
            session.persist(student2);

            tx.commit();
            tx = session.beginTransaction();

            course2Id = c.getId();

            final Course loadedCourse = session.get(Course.class, course2Id);

            System.out.println("\n\nCourse loaded:" + loadedCourse.getName());

            final Query<Student> queryStudents = session.createQuery("from Student where course = :courseParam", Student.class);
            queryStudents.setParameter("courseParam", loadedCourse);
            final List<Student> studentList = queryStudents.list();
            studentList.forEach(element -> {
                System.out.println("Student: " + element.getName());
            });

            tx.commit();
        } catch (Exception ex) {
            if (tx != null && !tx.getRollbackOnly()) {
                tx.rollback();
            }
            throw ex;
        }

        try (Session session = hibernateFactory.openSession()) {
            tx = session.beginTransaction();

            System.out.print("\n---------------------\nTest students set\n");

            final Course loadedCourse2 = session.get(Course.class, course2Id);

            loadedCourse2.getStudents().forEach(student -> {
                System.out.println(student.getName());
            });


            tx.commit();
        } catch (Exception ex) {
            if (tx != null && !tx.getRollbackOnly()) {
                tx.rollback();
            }
            throw ex;
        }



            System.out.println("\n---------------------\nTest code completed\n\n");

    }
}
