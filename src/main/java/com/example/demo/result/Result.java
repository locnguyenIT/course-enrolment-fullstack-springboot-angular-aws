package com.example.demo.result;

import com.example.demo.course.Course;
import com.example.demo.user.User;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Result  {

    @EmbeddedId
    private ResultID resultID;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "result_user_fk"))
    private User user;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "result_course_fk"))
    private Course course;

    @Column(name = "grade",columnDefinition = "INT",nullable = false)
    private Integer grade;

}
