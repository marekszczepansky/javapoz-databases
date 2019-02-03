package pl.szczepanski.marek.demo.databases.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public String name;

    public LocalDate startDate;

    @ManyToMany(mappedBy = "courses")
    public Set<Student> students;

}
