import { Teacher } from './../interface/teacher';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';


@Injectable({
  providedIn: 'root'
})
export class TeacherService {
  private serverURL = environment.serverURL;

  constructor(private http: HttpClient) { }

  public getListTeacher(): Observable<Teacher[]> {
    return this.http.get<Teacher[]>(`${this.serverURL}/api/teacher`);
  }

  public addTeacher(formData: FormData): Observable<void> {
    return this.http.post<void>(`${this.serverURL}/api/teacher/add`,formData)
  }

  public deleteTeacher(teacherId: number): Observable<void> {
    return this.http.delete<void>(`${this.serverURL}/api/teacher/delete/teacherId/${teacherId}`);
  }

  public updateTeacher(teacherId: number,formData: FormData): Observable<void> {
    return this.http.put<void>(`${this.serverURL}/api/teacher/update/teacherId/${teacherId}`,formData)
  }
}
