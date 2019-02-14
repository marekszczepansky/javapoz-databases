package pl.szczepanski.marek.demo.databases;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.szczepanski.marek.demo.databases.entities.Course;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RestController
public class BaseController {


    @PersistenceContext
    private EntityManager em;

    @GetMapping("students")
    public List getStudents() {
        List students = em.createQuery("select s from Student s").getResultList();
        return students;
    }

    @GetMapping("courses")
    public List getCourses() {
        List courses = em.createQuery("from Course").getResultList();
        return courses;
    }

    @GetMapping("course/{id}")
    public Course getCourse(@PathVariable Integer id) {
        Course course = em.find(Course.class, id);
        return course;
    }
}
