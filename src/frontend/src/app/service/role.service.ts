import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';
import { Role } from '../interface/role';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  private serverURL = environment.serverURL;

  constructor(private http: HttpClient) { }

  public getListRole(): Observable<Role[]> {
    return this.http.get<Role[]>(`${this.serverURL}/api/role`)
  }
  
}
