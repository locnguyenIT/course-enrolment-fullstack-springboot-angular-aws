import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/interface/user';
import { AuthService } from 'src/app/service/auth.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  user: User;

  constructor(private authService: AuthService, 
              private router: Router,
              private userService: UserService) { }

  ngOnInit(): void {
    this.getUser();
  }

  public signOut():void
  {
    this.authService.removeTokenUser();
    this.router.navigateByUrl('login');
  }

  public getUser():void {
    this.userService.getUser(this.authService.getUserId()).subscribe(
      {
        next: (resposne: User) => {
          this.user = resposne;
          console.log(this.user);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.message);
        },
        complete: () => {
          console.log('Done');
        }
      }
    )
  }

}
