package com.example.demo.course;

import com.example.demo.enrolment.Enrolment;
import com.example.demo.result.Result;
import com.example.demo.teacher.Teacher;
import com.example.demo.category.Category;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "course",uniqueConstraints = {@UniqueConstraint(name = "course_name_unique",
                                                              columnNames = "name")
})
public class Course {

    @Id
    @SequenceGenerator(name = "course_sequence",sequenceName = "course_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "course_sequence")
    @Column(name = "id",nullable = false)
    private Integer id;

    @NotEmpty(message = "Name should not be empty")
    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "imageURL",columnDefinition = "TEXT",nullable = false)
    private String imageURL;

    @Column(name = "start_at",columnDefinition = "DATETIME",nullable = false)
    private LocalDateTime start_at;

    @Column(name = "end_at",columnDefinition = "DATETIME",nullable = false)
    private LocalDateTime end_at;

    @ManyToOne
    @JoinColumn(name = "teacher_id",
                referencedColumnName = "id",
                nullable = false,
                foreignKey = @ForeignKey(name = "course_teacher_fk"))
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "category_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "course_category_fk"))
    private Category category;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<Enrolment> enrolments = new ArrayList<>();

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<Result> results = new ArrayList<>();

    public Course(String name, String imageURL,LocalDateTime start_at, LocalDateTime end_at, Teacher teacher, Category category) {
        this.name = name;
        this.imageURL = imageURL;
        this.start_at = start_at;
        this.end_at = end_at;
        this.teacher = teacher;
        this.category = category;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public LocalDateTime getStart_at() {
        return start_at;
    }

    public void setStart_at(LocalDateTime start_at) {
        this.start_at = start_at;
    }

    public LocalDateTime getEnd_at() {
        return end_at;
    }

    public void setEnd_at(LocalDateTime end_at) {
        this.end_at = end_at;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", start_at=" + start_at +
                ", end_at=" + end_at +
                ", teacher=" + teacher +
                ", type=" + category +
                '}';
    }
}
