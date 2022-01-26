package com.example.demo.enrolment;

import com.example.demo.course.Course;
import com.example.demo.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Enrolment {

    @EmbeddedId
    private EnrolmentID enrolmentID;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id",
                nullable = false,
                referencedColumnName = "id",
                foreignKey = @ForeignKey(name = "enrolment_user_fk"))
    private User user;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id",
                nullable = false,
                referencedColumnName = "id",
                foreignKey = @ForeignKey(name = "enrolment_course_fk"))
    private Course course;

    @Column(name = "create_at",columnDefinition = "DATETIME",nullable = false)
    private LocalDateTime create_at;

}
