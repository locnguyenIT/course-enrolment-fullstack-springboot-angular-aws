import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';
import { Result } from '../interface/result';

@Injectable({
  providedIn: 'root'
})
export class ResultService {

  private serverURL = environment.serverURL;
  
  constructor(private http: HttpClient) { }

  public getListResult(): Observable<Result[]> {
    return this.http.get<Result[]>(`${this.serverURL}/api/result`);
  }

  public addResult(userId: number, courseId: number, grade: number): Observable<void> {
    return this.http.post<void>(`${this.serverURL}/api/result/add/userId/${userId}/courseId/${courseId}/grade/${grade}`,null);
  }

  public deleteResult(userId: number, courseId: number): Observable<void> {
    return this.http.delete<void>(`${this.serverURL}/api/result/delete/userId/${userId}/courseId/${courseId}`);
  }

  public updateResult(userId: number, courseId: number, grade: number): Observable<void> {
    return this.http.put<void>(`${this.serverURL}/api/result/update/userId/${userId}/courseId/${courseId}/grade/${grade}`,null);
  }
}
