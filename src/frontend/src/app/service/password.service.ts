import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';

@Injectable({
  providedIn: 'root'
})
export class PasswordService {

  private serverURL = environment.serverURL;
  
  constructor(private http: HttpClient) { }

  public sendEmailToken(email: string): Observable<void>
  {
      // const param= new HttpParams().set('email',email)
      return this.http.post<void>(`${this.serverURL}/api/reset-password/send-token/email/${email}`,null);
  }

  public resetPassword(token:string,password: string): Observable<void>
  {
    
      return this.http.put<void>(`${this.serverURL}/api/reset-password/reset/token/${token}/password/${password}`,null);
  }
}
