import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { PasswordService } from 'src/app/service/password.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  public isEmaiValid = false;
  public isEmaiInvalid = false;
  public errorMessage: string;
  public isResetFailed = false;
  public isResetSuccess = false;

  constructor(private passwordService: PasswordService
            , private router: Router) { }

  ngOnInit(): void {
  }

  public sendEmailToken(sendEmailForm: NgForm): void
  {
    this.passwordService.sendEmailToken(sendEmailForm.value.email).subscribe(
      {
        next: (response: void) => {
          console.log(response);
          this.isEmaiValid = true;
          document.getElementById('div-resetpassword').style.display='block';
        },
        error: (err: HttpErrorResponse) => {
          this.errorMessage = err.error.message;
          this.isEmaiInvalid = true;
        }   
      }
    )  
  }

}
