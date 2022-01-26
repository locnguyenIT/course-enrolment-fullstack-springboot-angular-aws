import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';


@Injectable({
  providedIn: 'root'
})
export class AuthService {


  private serverURL = environment.serverURL;

  constructor(private http: HttpClient) { }

  public login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.serverURL}/api/login`,{username,password});
  }

  public signUp(name: string, email: string, password: string): Observable<void> {
    return this.http.post<void>(`${this.serverURL}/api/registration`,{name,email,password});
  }

  public saveToken(token: string): void{
    window.sessionStorage.setItem('token',token);
  }

  public saveUserId(userId: string):void {
    window.sessionStorage.setItem('userId',userId);
  }

  public getToken(): string {
    return window.sessionStorage.getItem('token');
  }

  public getUserId(): number {
    return parseInt(window.sessionStorage.getItem('userId'));
  }

  public removeTokenUser(): void {
      window.sessionStorage.removeItem('token');
      window.sessionStorage.removeItem('userId');
  }


}
