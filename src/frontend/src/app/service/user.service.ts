import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';
import { User } from '../interface/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private serverURL = environment.serverURL;

  constructor(private http: HttpClient) { }

  public getListUser(): Observable<User[]> {
    return this.http.get<User[]>(`${this.serverURL}/api/user`);
  }

  public getUser(userId: number): Observable<User> {
    return this.http.get<User>(`${this.serverURL}/api/user/userId/${userId}`);
  }

  public uploadImageUserProfile(userId: number, formData:FormData): Observable<void> {
    return this.http.post<void>(`${this.serverURL}/api/user/upload/image/userId/${userId}`,formData);
  }


  public addUser(roleId: number,formData: FormData): Observable<void> {
    return this.http.post<void>(`${this.serverURL}/api/user/add/roleId/${roleId}`,formData);
  }

  public deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.serverURL}/api/user/delete/userId/${userId}`);
  }

  // public updateUser(userId: number, formData: FormData): Observable<User> {
  //   return this.http.put<User>(`${this.serverURL}/api/user/update/userId/${userId}`,formData);
  // }

  public updateUser(userId: number,userProfileId: number, formData: FormData): Observable<User> {
    return this.http.put<User>(`${this.serverURL}/api/user/update/userId/${userId}/userProfileId/${userProfileId}`,formData);
  }

  public updateUserProfile(userId: number, formData: FormData): Observable<User> {
    return this.http.put<User>(`${this.serverURL}/api/user/update/user-profile/userId/${userId}`,formData);
  }
}
