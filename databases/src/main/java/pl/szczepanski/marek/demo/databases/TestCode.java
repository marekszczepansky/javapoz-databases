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
import java.time.LocalDate;
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

        int course2Id;
        Transaction tx = null;
        try (Session session = hibernateFactory.openSession()) {
            tx = session.beginTransaction();

            Course course1 = new Course();
            course1.setName("code 1");
            session.persist(course1);

            Course course2 = new Course();
            course2.setName("code 2");
            session.persist(course2);
            tx.commit();

            tx = session.beginTransaction();
            Student student = new Student();
            student.setName("John Brown");
            student.setAddress("2nd avenue 1234");
            student.setCourse(course2);
            session.persist(student);

            Student student2 = new Student();
            student2.setName("Jan Smith");
            student2.setAddress("2nd avenue 1234");
            student2.setCourse(course2);
            session.persist(student2);

            course2.setStartDate(LocalDate.now());
            tx.commit();
            course2Id = course2.getId();

            tx = session.beginTransaction();

            Course courseLoaded = session.get(Course.class, course2Id);
            System.out.println(
                    "\n\nCourse loaded: " +
                            courseLoaded.getName()
            );

            Query<Student> queryStudents = session.createQuery(
                    "from Student where course = :course",
                    Student.class
            );
            queryStudents.setParameter("course", courseLoaded);
            List<Student> list = queryStudents.list();
            for (Student element : list) {
                System.out.println("Student: " + element.getName());
            }
            tx.commit();

        } catch (Exception ex) {
            if (tx != null && !tx.getRollbackOnly()) {
                tx.rollback();
            }
            throw ex;
        }

        try (Session session = hibernateFactory.openSession()) {
            tx = session.beginTransaction();

            Course courseLoaded = session.get(Course.class, course2Id);

            System.out.println("Students from course set");
            for (Student element : courseLoaded.getStudents()) {
                System.out.println("Student: " + element.getName());
            }

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
