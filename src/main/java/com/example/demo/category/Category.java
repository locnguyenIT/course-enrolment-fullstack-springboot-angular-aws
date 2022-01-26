package com.example.demo.category;

import com.example.demo.course.Course;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@ToString
@Table(name = "category", uniqueConstraints = {
        @UniqueConstraint(name = "category_name_unique",columnNames = "name")
})
public class Category {

    @Id
    @SequenceGenerator(name = "category_sequence",sequenceName = "category_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "category_sequence")
    @Column(name = "id",nullable = false)
    private Integer id;

    @NotEmpty(message = "Name should not be empty")
    @Column(name = "name",nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "category")
    private List<Course> courseList = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
