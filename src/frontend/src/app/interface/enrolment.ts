import { Course } from "./course";
import { User } from "./user";

export interface Enrolment
{
   user: User;
   course: Course;
   create_at: Date;

}