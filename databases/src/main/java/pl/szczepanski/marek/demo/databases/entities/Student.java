package pl.szczepanski.marek.demo.databases.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public String name;

    public String address;

    @ManyToMany
    public Set<Course> courses;

    @Transient
    public String name_with_address;
}
