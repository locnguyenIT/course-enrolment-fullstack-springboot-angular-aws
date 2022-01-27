# Course Enrolment Management Application | Fullstack | Spring Boot | Angular
![login](https://user-images.githubusercontent.com/86077654/151330130-fea7a85d-6c6e-440e-9037-d96d8e6f74e7.png)
![course](https://user-images.githubusercontent.com/86077654/151330133-70a1710f-ed17-4b5a-b7bb-0db2f5e7eb27.png)
![user-profile](https://user-images.githubusercontent.com/86077654/151330135-b72738f0-7961-4fc1-aefa-3a563c8d1c19.png)
## Application run live
-Link: http://courseenrollmentmanagementapplicaion-env.eba-tkyegmya.ap-southeast-1.elasticbeanstalk.com/
## Project description
This project is an web application fullstack based on Spring Boot for the backend and Angular for the frontend. The goal is perform course enrolment for users 
## Process
![course-enrolment-process](https://user-images.githubusercontent.com/86077654/151339737-e835d989-6457-483c-9c00-b44da1db0caa.png)
- After I finished building both frontend and backend then i use frontend-maven-plugin and maven-resources-plugin from maven to package frontend & backend to a single jar.
- Next, using jib-maven-plugin to containerize project then build image and push image into docker hub and create docker-compose.yml to run docker container. 
- Finally, upload docker-compose.yml file into AWS Elastic Beanstalk (Docker platform), AWS RDS to manage database, configuation security group and from that i have a new application version.

## Technology
- Spring Boot
- Spring Security with JWT
- Spring Data JPA
- Spring MVC
- Hibernate
- Angular
- MySQL
- Maven
- Docker
- AWS (RDS & Elastic Beantalk & S3 & SES)

## ER Diagram
![course-erd-db](https://user-images.githubusercontent.com/86077654/151331653-bab70812-bf76-4826-b024-e1ca6e929907.png)

Good luck !
