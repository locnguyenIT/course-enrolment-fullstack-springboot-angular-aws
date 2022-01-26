package com.example.demo.teacher;

import com.example.demo.aws.AWSBucket;
import com.example.demo.aws.AWSFileStore;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.example.demo.aws.AWSBucket.BUCKET_NAME;
import static org.apache.http.entity.ContentType.*;

@AllArgsConstructor
@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final AWSFileStore awsFileStore;

    public List<Teacher> getListTeacher() {
        return teacherRepository.findAll();
    }

    public byte[] downloadImage(Integer teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() ->
                new NotFoundException("Teacher was not found"));
        String path = String.format("%s/teacher/teacherId-%d",AWSBucket.BUCKET_NAME.getBucketName(),teacher.getId());

        byte[] image = awsFileStore.download(path,teacher.getImageURL());

        return image;
    }

    public void addTeacher(String teacher, MultipartFile file) {
        //1. Read value and mapping to POJO course with ObjectMapper from param course
        Teacher teacherObject = new Teacher();
        try {
            System.out.println(teacher);
            teacherObject = new ObjectMapper().readValue(teacher, Teacher.class);
            //2. Check if the email already used by another teacher
            Optional<Teacher> teacherByEmail = teacherRepository.findByEmail(teacherObject.getEmail());
            if(teacherByEmail.isPresent()){
                throw new BadRequestException(String.format("Teacher with email '%s' already used",teacherObject.getName()));
            }
            //3. Check the file
            if (file != null){
                //Check the ContentType of file is image or not
                if (!Arrays.asList(IMAGE_JPEG.getMimeType(),
                                IMAGE_GIF.getMimeType(),
                                IMAGE_PNG.getMimeType())
                        .contains(file.getContentType())){
                    throw new IllegalStateException("File is not image");
                }
                //Set imageURL
                String filename = String.format("%s-%s",file.getOriginalFilename(), UUID.randomUUID());
                //4. Save
                teacherObject.setImageURL(filename);
                teacherRepository.save(teacherObject);
                //5. Upload image to AWS S3
                Map<String,String> metadata = new HashMap<>();
                metadata.put("Content-Type",file.getContentType());
                metadata.put("Content-Length", String.valueOf(file.getSize()));
                String path = String.format("%s/teacher/teacherId-%d", BUCKET_NAME.getBucketName(),teacherObject.getId());
                awsFileStore.upload(path,filename,file.getInputStream(), Optional.of(metadata));
            } else {
                //if file == null -> Set imageURL default
                teacherObject.setImageURL("teacher-default.png");
                teacherRepository.save(teacherObject);
                String bucket = String.format("%s/teacher",BUCKET_NAME.getBucketName());
                String folder = String.format("teacherId-%d",teacherObject.getId());
                awsFileStore.create(bucket,folder);
                //copy from "user-default/user-default.png" and pass to folder "userId-%d/user-default.png" just created teacher
                String sourceKey = "teacher-default/teacher-default.png";
                String destinationKey = String.format("teacherId-%d/teacher-default.png",teacherObject.getId());
                awsFileStore.coppy(bucket,sourceKey,destinationKey);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void deleteTeacher(Integer teacherId) {
        //1. Check the Teacher exists
        boolean existsCourse = teacherRepository.existsById(teacherId);
        if (!existsCourse){
            throw new NotFoundException("The teacher was not found");
        }
        //2. Delete
//        String bucket = String.format("%s", AWSBucket.BUCKET_NAME.getBucketName());
//        String key = String.format("teacher/teacherId-%d",teacherId);
//        awsFileStore.delete(bucket,key);
        teacherRepository.deleteById(teacherId);
    }

    @Transactional
    public void updateTeacher(Integer teacherId, String name, String email, String specialize, MultipartFile file) {
        //1. Check if the course exists
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(()->
                new NotFoundException("Teacher was not found"));
        //2. Check the name
        if(name != null && name.length() > 0 && !Objects.equals(teacher.getName(),name)) {
            teacher.setName(name);
        }
        //2. Check specialize
        if(specialize != null && specialize.length() > 0 && !Objects.equals(teacher.getSpecialize(),specialize)) {
            teacher.setSpecialize(specialize);
        }
        //3. Check if the email already used by another teacher
        if(email != null && email.length() > 0 && !Objects.equals(teacher.getEmail(),email)) {
            Optional<Teacher> teacherByEmail = teacherRepository.findByEmail(email);
            if(teacherByEmail.isPresent()){
                throw new BadRequestException(String.format("Teacher with email '%s' already used",email));
            }
            teacher.setEmail(email);
        }
        //4. Check the file
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
            String path = String.format("%s/teacher/teacherId-%d", AWSBucket.BUCKET_NAME.getBucketName(),teacher.getId());
            String filename = String.format("%s-%s",file.getOriginalFilename(),UUID.randomUUID());
            try {
                awsFileStore.upload(path,filename,file.getInputStream(), Optional.of(metadata));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            //Set imageURL
            teacher.setImageURL(filename);
        }
    }

}
