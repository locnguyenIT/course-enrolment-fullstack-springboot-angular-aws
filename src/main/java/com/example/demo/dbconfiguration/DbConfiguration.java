package com.example.demo.dbconfiguration;

import com.example.demo.course.Course;
import com.example.demo.course.CourseRepository;
import com.example.demo.enrolment.Enrolment;
import com.example.demo.enrolment.EnrolmentID;
import com.example.demo.enrolment.EnrolmentRepository;
import com.example.demo.result.Result;
import com.example.demo.result.ResultID;
import com.example.demo.result.ResultRepository;
import com.example.demo.role.Role;
import com.example.demo.role.RoleRepository;
import com.example.demo.teacher.Teacher;
import com.example.demo.teacher.TeacherRepository;
import com.example.demo.resetpassword.token.TokenRepository;
import com.example.demo.category.Category;
import com.example.demo.category.CategoryRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.demo.role.EnumRole.*;

@Configuration
public class DbConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository,
                                        RoleRepository roleRepository,
                                        CategoryRepository categoryRepository,
                                        TeacherRepository teacherRepository,
                                        CourseRepository courseRepository,
                                        EnrolmentRepository enrolmentRepository,
                                        ResultRepository resultRepository,
                                        BCryptPasswordEncoder passwordEncoder)
    {
        return args -> {

            //INSERT ROLE
            Role role_super_admin = new Role(SUPER_ADMIN);
            Role role_admin = new Role(ADMIN);
            Role role_user = new Role(USER);
            roleRepository.saveAll(List.of(role_super_admin,role_admin,role_user));

            //INSERT USER
            User loc = new User("Loc","ntloc.developer@gmail.com",passwordEncoder.encode("password123456"),"ntloc.png","Khanh Hoa",LocalDateTime.now(),true,role_super_admin);
            User admin = new User("Admin","admin@gmail.com",passwordEncoder.encode("password123456"),"userId-2.png","Ho Chi Minh",LocalDateTime.now(),true,role_admin);
            User ngoc = new User("Ngoc","ngoc@gmail.com",passwordEncoder.encode("password123456"),"userId-3.png","Dong Nai",LocalDateTime.now(),true,role_user);
            User vinh = new User("Vinh","vinh@gmail.com",passwordEncoder.encode("password123456"),"hello.gif","Dong Nai",LocalDateTime.now(),true,role_user);
            userRepository.saveAll(List.of(loc,admin,ngoc,vinh));

            //INSERT CATEGORY
            Category frontend = new Category("Frontend");
            Category backend = new Category("Backend");
            Category fullstack = new Category("Fullstack");
            Category dev_ops = new Category("DevOps");
            categoryRepository.saveAll(List.of(frontend,backend,fullstack,dev_ops));

            //INSERT TEACHER
            Teacher hoang = new Teacher("Ho Viet Hoang","hoang@gmail.com","teacher-1.jpg","Software Engineer");
            Teacher nam = new Teacher("Do Hoang Nam","name@gmail.com","teacher-2.jpg","Backend Developer");
            Teacher linh = new Teacher("Tran Ha Linh","linh@gmail.com","teacher-3.jpg","Frontend Developer");
            Teacher huy = new Teacher("Tran Hoang Huy","huy@gmail.com","teacher-4.jpg","Fullstack Developer");
            teacherRepository.saveAll(List.of(hoang,nam,linh,huy));

            //INSERT COURSE
            Course angular = new Course("Angular","angular.png",
                                        LocalDateTime.of(2022,1,11,7,00,00),
                                        LocalDateTime.of(2022,1,17,17,00,00),
                                        linh,
                                        frontend);
            Course springboot = new Course("Spring Boot","springboot.png",
                                            LocalDateTime.of(2022,1,11,7,00,00),
                                            LocalDateTime.of(2022,1,17,17,00,00),
                                            nam,
                                            backend);
            Course fullstack_angular_springboot = new Course("Spring Boot & Angular","springboot-angular-fullstack.png",
                                                        LocalDateTime.of(2022,1,11,7,00,00),
                                                        LocalDateTime.of(2022,1,17,17,00,00),
                                                            huy,
                                                            fullstack);
            Course docker = new Course("Docker","docker.png",
                    LocalDateTime.of(2022,1,23,7,00,00),
                    LocalDateTime.of(2022,1,28,17,00,00),
                    hoang,
                    dev_ops);
            courseRepository.saveAll(List.of(angular,springboot,fullstack_angular_springboot,docker));

            //INSERT ENROLMENT
            Enrolment enrolment_1 = new Enrolment(new EnrolmentID(4,2),
                                                    vinh,
                                                    springboot,
                                                    LocalDateTime.of(2022,01,13,8,20,15));
            Enrolment enrolment_2 = new Enrolment(new EnrolmentID(3,1),
                                                    ngoc,
                                                    angular,
                                                    LocalDateTime.of(2022,01,12,9,00,20));
            enrolmentRepository.saveAll(List.of(enrolment_1,enrolment_2));

            //INSERT RESULT
            Result result_1 = new Result(new ResultID(4,2),vinh,springboot,8) ;
            Result result_2 = new Result(new ResultID(3,1),ngoc,angular,10) ;
            Result result_3 = new Result(new ResultID(4,3),vinh,fullstack_angular_springboot,6) ;
            Result result_4 = new Result(new ResultID(3,3),ngoc,springboot,9) ;
            resultRepository.saveAll(List.of(result_1,result_2,result_3,result_4));
        };
    }
}
