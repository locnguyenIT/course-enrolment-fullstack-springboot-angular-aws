import { Course } from "./course";
import { User } from "./user";

export interface Result
{
    user: User;
    course: Course;
    grade: number;
}