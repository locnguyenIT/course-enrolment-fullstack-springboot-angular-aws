package com.example.demo.course;

import com.example.demo.aws.AWSBucket;
import com.example.demo.aws.AWSFileStore;
import com.example.demo.category.Category;
import com.example.demo.category.CategoryRepository;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.teacher.Teacher;
import com.example.demo.teacher.TeacherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.demo.aws.AWSBucket.BUCKET_NAME;
import static org.apache.http.entity.ContentType.*;

@AllArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final TeacherRepository teacherRepository;
    private final AWSFileStore awsFileStore;

    public List<Course> getListCourse() {
        return courseRepository.findAll();
    }

    public List<Course> getListCourseOfCategory(Integer categoryId) {
        return courseRepository.findByCategoryId(categoryId);
    }

    public List<Course> getListCourseOfTeacher(Integer teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }


    public byte[] downloadImage(Integer courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() ->
                new NotFoundException("Course was not found"));
        String path = String.format("%s/course/courseId-%d",AWSBucket.BUCKET_NAME.getBucketName(),course.getId());

        byte[] image = awsFileStore.download(path,course.getImageURL());

        return image;
    }

    public void addCourse(Integer categoryId, Integer teacherId, String course, MultipartFile file) {
        //1. Find the category
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->
                new NotFoundException("Category was not found"));
        //2. Find the teacher
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(()->
                new NotFoundException("Teacher was not found"));
        //3. Read value and mapping to POJO course with ObjectMapper from param course
        Course courseObject = new Course();
        try {
            System.out.println(course);
            courseObject = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(course,Course.class);
            //4. Check if the name already used by another course
            Optional<Course> coursebyName = courseRepository.findByName(courseObject.getName());
            if(coursebyName.isPresent()){
                throw new BadRequestException(String.format("Course with name %s already exists",courseObject.getName()));
            }
            //5. Check the start_at must be before end_at
            if (!(courseObject.getStart_at().isBefore(courseObject.getEnd_at()))){
                throw new BadRequestException("Oops! The start date must be before the end date");
            }
            //6. Check the file, save and upload to AWS S3
            if (file != null){
                //Check the ContentType of file is image or not
                if (!Arrays.asList(IMAGE_JPEG.getMimeType(),
                                IMAGE_GIF.getMimeType(),
                                IMAGE_PNG.getMimeType())
                                .contains(file.getContentType())){
                    throw new IllegalStateException("File is not image");
                }
                //Set imageURL
                String filename = String.format("%s-%s",file.getOriginalFilename(),UUID.randomUUID());
                //7. Save
                courseObject.setImageURL(filename);
                courseObject.setCategory(category);
                courseObject.setTeacher(teacher);
                courseRepository.save(courseObject);
                //8. Upload image to AWS S3
                Map<String,String> metadata = new HashMap<>();
                metadata.put("Content-Type",file.getContentType());
                metadata.put("Content-Length", String.valueOf(file.getSize()));
                String path = String.format("%s/course/courseId-%d", AWSBucket.BUCKET_NAME.getBucketName(),courseObject.getId());
                awsFileStore.upload(path,filename,file.getInputStream(), Optional.of(metadata));

            } else {
                //if file == null -> Set imageURL default & Save
                courseObject.setImageURL("course-default.png");
                courseObject.setCategory(category);
                courseObject.setTeacher(teacher);
                courseRepository.save(courseObject);
                //create new folder for user
                String bucket = String.format("%s/course",BUCKET_NAME.getBucketName());
                String folder = String.format("courseId-%d",courseObject.getId());
                awsFileStore.create(bucket,folder);
                //copy from "user-default/user-default.png" and pass to folder "userId-%d/user-default.png" just created course
                String sourceKey = "course-default/course-default.png";
                String destinationKey = String.format("courseId-%d/course-default.png",courseObject.getId());
                awsFileStore.coppy(bucket,sourceKey,destinationKey);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void deleteCourse(Integer courseId) {
        //1. Check the Course exists
        boolean existsCourse = courseRepository.existsById(courseId);
        if (!existsCourse){
            throw new NotFoundException("The course was not found");
        }
        //2. Delete
//        String bucket = String.format("%s", AWSBucket.BUCKET_NAME.getBucketName());
//        String key = String.format("course/courseId-%d",courseId);
//        awsFileStore.delete(bucket,key);
        courseRepository.deleteById(courseId);
    }

    @Transactional
    public void updateCourse(Integer courseId, String name, LocalDateTime start_at, LocalDateTime end_at,
                             Integer categoryId, Integer teacherId, MultipartFile file) {
        //1. Find the course
        Course course = courseRepository.findById(courseId).orElseThrow(()->
                new NotFoundException("The course was not found"));
        //2. Check if the name already used by another course
        if(name != null && name.length() > 0 && !Objects.equals(course.getName(),name)) {
            Optional<Course> courseByName = courseRepository.findByName(name);
            if(courseByName.isPresent()){
                throw new BadRequestException(String.format("Course with name %s already exists",name));
            }
            course.setName(name);
        }
        //3. Check the start_at must be before the end_at
        if (start_at != null && end_at != null){
            if (start_at.isBefore(end_at)) {
                course.setStart_at(start_at);
                course.setEnd_at(end_at);
            } else {
                throw new BadRequestException("The date start of the course must be before the end date");
            }
        }
        //4. Check the category
        if (categoryId != null && !Objects.equals(course.getCategory().getId(),categoryId)) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                    new NotFoundException("The category was not found"));
            course.setCategory(category);
        }

        //5. Check the teacher
        if (teacherId != null && !Objects.equals(course.getTeacher().getId(),teacherId)) {
            Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(()->
                    new NotFoundException("Teacher was not found"));
            course.setTeacher(teacher);
        }
        //6. Check the file
        if (file != null){
            //Check the ContentType of file is image or not
            if (!Arrays.asList(IMAGE_JPEG.getMimeType(),
                            IMAGE_GIF.getMimeType(),
                            IMAGE_PNG.getMimeType())
                    .contains(file.getContentType())){
                throw new IllegalStateException("File is not image");
            }
            //upload image to AWS S3
            Map<String,String> metadata = new HashMap<>();
            metadata.put("Content-Type",file.getContentType());
            metadata.put("Content-Length", String.valueOf(file.getSize()));
            String path = String.format("%s/course/courseId-%d", AWSBucket.BUCKET_NAME.getBucketName(),course.getId());
            String filename = String.format("%s-%s",file.getOriginalFilename(),UUID.randomUUID());
            try {
                awsFileStore.upload(path,filename,file.getInputStream(), Optional.of(metadata));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            //Set imageURL
            course.setImageURL(filename);
        }
    }



}
