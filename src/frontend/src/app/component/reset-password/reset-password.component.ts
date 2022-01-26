import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { PasswordService } from 'src/app/service/password.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

  public errorMessage: string;
  public isResetFailed = false;
  public isResetSuccess = false;
  constructor(private passwordService: PasswordService, private router: Router) { }

  ngOnInit(): void {
  }

  public resetPassword(resetForm: NgForm): void
  {
    this.passwordService.resetPassword(resetForm.value.token,
                                      resetForm.value.password).subscribe(
      {
        next: (response: void) => {
          console.log(response);
          this.isResetSuccess = true;
        },
        error: (err: HttpErrorResponse) => {
          this.errorMessage = err.error.message;
          this.isResetFailed = true;
        }   
      }
    )
  }

  public backToLogIn(): void
  {
    this.router.navigateByUrl('login');
  }
}
