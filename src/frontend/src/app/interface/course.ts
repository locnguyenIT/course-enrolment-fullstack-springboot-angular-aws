import { Category } from "./category";
import { Teacher } from "./teacher";

export interface Course
{
    id: number;
    name: string;
    imageURL: string;
    start_at: Date;
    end_at: Date;
    teacher: Teacher;
    category: Category;
}