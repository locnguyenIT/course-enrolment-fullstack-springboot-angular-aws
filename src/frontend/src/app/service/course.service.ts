import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';
import { Course } from '../interface/course';

@Injectable({
  providedIn: 'root'
})
export class CourseService {

  private serverURL = environment.serverURL;
  
  constructor(private http: HttpClient) { }

  public getListCourse(): Observable<Course[]>
  {
    return this.http.get<Course[]>(`${this.serverURL}/api/course`);
  }

  public getListCourseOfCatefory(categoryId: string): Observable<Course[]>
  {
    const id = parseInt(categoryId);
    return this.http.get<Course[]>(`${this.serverURL}/api/course/category/categoryId/${id}`);
  }

  public getListCourseOfTeacher(teacherId: string): Observable<Course[]>
  {
    const id = parseInt(teacherId);
    return this.http.get<Course[]>(`${this.serverURL}/api/course/teacher/teacherId/${id}`);
  }

  public addCourse(categoryId: number,teacherId: number,formData: FormData): Observable<void> {
    return this.http.post<void>(`${this.serverURL}/api/course/add/categoryId/${categoryId}/teacherId/${teacherId}`,formData);
  }

  public deleteCourse(courseId: number): Observable<void> {
    return this.http.delete<void>(`${this.serverURL}/api/course/delete/courseId/${courseId}`);
  }

  public updateCourse(courseId: number,formData: FormData): Observable<void> {
    return this.http.put<void>(`${this.serverURL}/api/course/update/courseId/${courseId}`,formData);
  }
}
