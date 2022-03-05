package com.example.demo.result;

import com.example.demo.category.Category;
import com.example.demo.category.CategoryRepository;
import com.example.demo.course.Course;
import com.example.demo.course.CourseRepository;
import com.example.demo.role.Role;
import com.example.demo.role.RoleRepository;
import com.example.demo.teacher.Teacher;
import com.example.demo.teacher.TeacherRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

import static com.example.demo.role.EnumRole.SUPER_ADMIN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ResultRepositoryTest {

    @Autowired
    private UserRepository underTestUser;
    @Autowired
    private RoleRepository underTestRole;
    @Autowired
    private CourseRepository underTestCourse;
    @Autowired
    private CategoryRepository underTestCategory;
    @Autowired
    private TeacherRepository underTestTeacher;
    @Autowired
    private ResultRepository underTestResult;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @AfterEach
    void tearDown() {
        underTestRole.deleteAll();
        underTestCategory.deleteAll();
        underTestTeacher.deleteAll();
    }

    @Test
    void itShouldCheckIfResultExistsByUserIdAndCourseId() {
        // Given
        Role role_super_admin = new Role(SUPER_ADMIN);
        underTestRole.save(role_super_admin);

        User user = new User("Loc",
                "ntloc.developer@gmail.com",
                passwordEncoder.encode("password123456"),
                "ntloc.png",
                "Khanh Hoa", LocalDateTime.now(),
                true,
                role_super_admin);
        underTestUser.save(user);

        Category category = new Category("Frontend");
        underTestCategory.save(category);

        Teacher teacher = new Teacher("Ho Viet Hoang",
                                    "hoang@gmail.com",
                                    "teacher-1.jpg",
                                    "Software Engineer");
        underTestTeacher.save(teacher);

        Course course = new Course("Angular","angular.png",
                LocalDateTime.of(2022,1,11,7,00,00),
                LocalDateTime.of(2022,1,17,17,00,00),
                teacher,
                category);

        underTestCourse.save(course);

        Result result = new Result(new ResultID(1,1),user,course,8);
        underTestResult.save(result);
        // When
        boolean existsResultByUserIdAndCourseId = underTestResult.existsByUserIdAndCourseId(user.getId(), course.getId());
        // Then
        assertThat(existsResultByUserIdAndCourseId).isTrue();
    }

    @Test
    void itShouldCheckIfResultDoesNotExistsByUserIdAndCourseId() {
        // Given
        Role role_super_admin = new Role(SUPER_ADMIN);
        underTestRole.save(role_super_admin);

        User user = new User("Loc",
                "ntloc.developer@gmail.com",
                passwordEncoder.encode("password123456"),
                "ntloc.png",
                "Khanh Hoa", LocalDateTime.now(),
                true,
                role_super_admin);
        underTestUser.save(user);

        Category category = new Category("Frontend");
        underTestCategory.save(category);

        Teacher teacher = new Teacher("Ho Viet Hoang",
                "hoang@gmail.com",
                "teacher-1.jpg",
                "Software Engineer");
        underTestTeacher.save(teacher);

        Course course = new Course("Angular","angular.png",
                LocalDateTime.of(2022,1,11,7,00,00),
                LocalDateTime.of(2022,1,17,17,00,00),
                teacher,
                category);

        underTestCourse.save(course);
        // When
        boolean existsResultByUserIdAndCourseId = underTestResult.existsByUserIdAndCourseId(user.getId(), course.getId());
        // Then
        assertThat(existsResultByUserIdAndCourseId).isFalse();

    }

    @Test
    @Disabled
    void itShouldDeleteResultByUserIdAndCourseId() {
        // Given
        Role role_super_admin = new Role(SUPER_ADMIN);
        underTestRole.save(role_super_admin);

        User user = new User("Loc",
                "ntloc.developer@gmail.com",
                passwordEncoder.encode("password123456"),
                "ntloc.png",
                "Khanh Hoa", LocalDateTime.now(),
                true,
                role_super_admin);
        underTestUser.save(user);

        Category category = new Category("Frontend");
        underTestCategory.save(category);

        Teacher teacher = new Teacher("Ho Viet Hoang",
                "hoang@gmail.com",
                "teacher-1.jpg",
                "Software Engineer");
        underTestTeacher.save(teacher);

        Course course = new Course("Angular","angular.png",
                LocalDateTime.of(2022,1,11,7,00,00),
                LocalDateTime.of(2022,1,17,17,00,00),
                teacher,
                category);

        underTestCourse.save(course);

        Result result = new Result(new ResultID(1,1),user,course,8);
        underTestResult.save(result);

        // When
        underTestResult.deleteByUserIdAndCourseId(user.getId(), course.getId());
        // Then
    }

    @Test
    @Disabled
    void updateGradeResultByUserIdAndCourseId() {
    }
}