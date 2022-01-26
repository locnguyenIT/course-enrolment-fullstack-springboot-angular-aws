package com.example.demo.teacher;

import com.example.demo.course.Course;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@ToString
@Entity
@Table(name = "teacher",uniqueConstraints = {
        @UniqueConstraint(name = "teacher_email_unique",columnNames = "email")
})
public class Teacher {

    @Id
    @SequenceGenerator(name = "teacher_sequence",sequenceName = "teacher_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "teacher_sequence")
    @Column(name = "id",nullable = false)
    private Integer id;

    @NotEmpty(message = "Name should not be empty")
    @Column(name = "name",columnDefinition = "TEXT",nullable = false)
    private String name;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email invalid")
    @Column(name = "email",nullable = false)
    private String email;

    @Column(name = "imageURL",columnDefinition = "TEXT",nullable = false)
    private String imageURL;

    @NotEmpty(message = "Specialize should not be empty")
    @Column(name = "specialize",columnDefinition = "TEXT",nullable = false)
    private String specialize;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "teacher")
    private List<Course> courses = new ArrayList<>();

    public Teacher(String name, String email, String imageURL,String specialize) {
        this.name = name;
        this.email = email;
        this.imageURL = imageURL;
        this.specialize = specialize;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSpecialize() {
        return specialize;
    }

    public void setSpecialize(String specialize) {
        this.specialize = specialize;
    }
}
