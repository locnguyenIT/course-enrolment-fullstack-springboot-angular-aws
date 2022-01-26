import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';
import { Enrolment } from '../interface/enrolment';

@Injectable({
  providedIn: 'root'
})
export class EnrolmentService {

  private serverURL = environment.serverURL;
  
  constructor(private http: HttpClient) { }

  public getListEnrolment(): Observable<Enrolment[]> {
    return this.http.get<Enrolment[]>(`${this.serverURL}/api/enrolment`);
  }

  public getListEnrolmentOfUser(userId: number): Observable<Enrolment[]> {
    return this.http.get<Enrolment[]>(`${this.serverURL}/api/enrolment/user/userId/${userId}`);
  }

  public addEnrolment(userId: number, courseId: number, create_at: string): Observable<void> {
    return this.http.post<void>(`${this.serverURL}/api/enrolment/add/userId/${userId}/courseId/${courseId}/create_at/${create_at}`,null);
  }

  public deleteEnrolment(userId: number, courseId: number): Observable<void> {
    return this.http.delete<void>(`${this.serverURL}/api/enrolment/delete/userId/${userId}/courseId/${courseId}`);
  }

  
}
