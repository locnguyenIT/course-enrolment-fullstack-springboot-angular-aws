package com.example.demo.course;

import com.example.demo.category.Category;
import com.example.demo.category.CategoryRepository;
import com.example.demo.teacher.Teacher;
import com.example.demo.teacher.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest //spin up h2 database for testing
class CourseRepositoryTest {

    @Autowired
    private CourseRepository underTestCourse;
    @Autowired
    private TeacherRepository underTestTeacher;
    @Autowired
    private CategoryRepository underTestCategory;

    @AfterEach // After each test, delete all data -> clean state
    void tearDown() {
        underTestCourse.deleteAll();
        underTestTeacher.deleteAll();
        underTestCategory.deleteAll();
    }

    @Test
    void itShouldCheckIfCoursePresentByName() {
        // Given
        Teacher hoang = new Teacher("Ho Viet Hoang","hoang@gmail.com","teacher-1.jpg","Software Engineer");
        underTestTeacher.save(hoang);

        Category frontend = new Category("Frontend");
        underTestCategory.save(frontend);

        String name = "OOP";
        Course course = new Course(name,"oop.jpg",
                        LocalDateTime.of(2022,02,21,07,00,00),
                        LocalDateTime.of(2022,02,21,17,00,00),
                        hoang,
                        frontend);
        underTestCourse.save(course);
        // When
        Optional<Course> courseByName = underTestCourse.findByName(name);
        // Then
        assertThat(courseByName).isPresent();
    }

    @Test
    void itShouldCheckIfCourseNotPresentByName() {
        // Given
        String name = "OOP";
        // When
        Optional<Course> courseByName = underTestCourse.findByName(name);
        // Then
        assertThat(courseByName).isNotPresent();
    }

    @Test
    void itShouldCheckIfCourseListByCategoryId() {
        // Given
        Teacher hoang = new Teacher("Ho Viet Hoang",
                                "hoang@gmail.com",
                                "teacher-1.jpg",
                                "Software Engineer");
        underTestTeacher.save(hoang);

        Category frontend = new Category("Frontend");
        underTestCategory.save(frontend);

        String name = "OOP";
        Course course = new Course(name,"oop.jpg",
                LocalDateTime.of(2022,02,21,07,00,00),
                LocalDateTime.of(2022,02,21,17,00,00),
                hoang,
                frontend);
        underTestCourse.save(course);
        // When
        List<Course> listCourseByCategoryId = underTestCourse.findByCategoryId(frontend.getId());
        // Then
        assertThat(listCourseByCategoryId).isEqualTo(listCourseByCategoryId);
    }

    @Test
    void itShouldCheckIfCourseListByTeacherId() {
        // Given
        Teacher teacher = new Teacher("Ho Viet Hoang",
                "hoang@gmail.com",
                "teacher-1.jpg",
                "Software Engineer");
        underTestTeacher.save(teacher);

        Category frontend = new Category("Frontend");
        underTestCategory.save(frontend);

        String name = "OOP";
        Course course = new Course(name,"oop.jpg",
                LocalDateTime.of(2022,02,21,07,00,00),
                LocalDateTime.of(2022,02,21,17,00,00),
                teacher,
                frontend);
        underTestCourse.save(course);
        // When
        List<Course> listCourseByTeacherId = underTestCourse.findByTeacherId(teacher.getId());
        // Then
        assertThat(listCourseByTeacherId).isEqualTo(listCourseByTeacherId);
    }

}