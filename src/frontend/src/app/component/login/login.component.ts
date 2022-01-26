import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  errMessage: string;
  isLoginFailed: boolean = false;
  isSignupFailed: boolean = false
  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  public login (loginForm: NgForm):void {
    this.authService.login(loginForm.value.username,loginForm.value.password).subscribe(
      {
        next: (response: any) => {
          console.log(response);
          this.authService.saveToken(response.token);
          this.authService.saveUserId(response.userId);
          alert('Login successfully');
          this.router.navigateByUrl('menu/course');
        },
        error: (err: HttpErrorResponse) => {
          this.errMessage = err.error.message;
          this.isLoginFailed = true;
        },
        complete: () => {
          console.log('Done');
        }
      }
    )
  }

  public signUp(signUpForm: NgForm):void {
    this.authService.signUp(signUpForm.value.name,
                            signUpForm.value.email,
                            signUpForm.value.password).subscribe(
      {
        next: (response: void) => {
          console.log(response);
          alert('Sign Up successfully');
          document.getElementById('btn-close-modal').click();
        },
        error: (err: HttpErrorResponse) => {
          this.errMessage = err.error.message;
          this.isSignupFailed = true;
        },
        complete: () => {
          console.log('Done');
        }
      }
    )
  }

}
