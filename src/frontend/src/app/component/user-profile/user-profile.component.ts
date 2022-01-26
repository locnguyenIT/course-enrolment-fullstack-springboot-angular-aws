import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Enrolment } from 'src/app/interface/enrolment';
import { Role } from 'src/app/interface/role';
import { User } from 'src/app/interface/user';
import { AuthService } from 'src/app/service/auth.service';
import { EnrolmentService } from 'src/app/service/enrolment.service';
import { RoleService } from 'src/app/service/role.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  selectedFile: File;
  user: User;
  listRole: Role[] = [];
  listEnrolmentOfUser: Enrolment[] = [];

  constructor(private authService: AuthService,
             private roleService: RoleService,
             private enrolmentService: EnrolmentService,
             private userService: UserService) { }

  ngOnInit(): void {
    this.getUser();
    this.getListRole();
    this.getListEnrolmentOfUser();
  }

  public getListRole():void{
    this.roleService.getListRole().subscribe(
      {
        next: (response: Role[]) => {
          this.listRole = response;
          console.log(this.listRole);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.message);
        }
      }
    )
  }

  public changePhoto():void{
    document.getElementById('button-file').click();
  }

  public getUser(): void {
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

  public fileSelected(event: any): void{
    this.selectedFile = event.target.files[0];
    console.log(this.selectedFile);
    if(this.selectedFile !== null) {
      this.uploadImageUserProfile();    
    }
  }

  public getListEnrolmentOfUser(): void {
    this.enrolmentService.getListEnrolmentOfUser(this.authService.getUserId()).subscribe(
      {
        next: (response: Enrolment[]) => {
          this.listEnrolmentOfUser = response;
          console.log(this.listEnrolmentOfUser);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.message);
        },
        complete: () => {
          console.log("Done");
        }
      }
    )
  }

  public updateUserProfile(userProfileForm: NgForm): void {
    const formData = new FormData();
    formData.append('name',userProfileForm.value.name);
    formData.append('email',userProfileForm.value.email);
    formData.append('address',userProfileForm.value.address);
    formData.append('enable',userProfileForm.value.enable);
    formData.append('roleId',userProfileForm.value.roleId);

    this.userService.updateUserProfile(userProfileForm.value.id,formData).subscribe(
      {
        next: (response: User) => {
          console.log(response);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.message);
          window.location.reload();
        },
        complete: () => {
          console.log('Done');
          alert('Update successfully');
          window.location.reload();
        }
      }
    ) 
  }

  public uploadImageUserProfile():void {
    const formData = new FormData();
    formData.append('file',this.selectedFile);
    this.userService.uploadImageUserProfile(this.authService.getUserId(),formData).subscribe(
      {
        next: (resposne: void) => {
            console.log(resposne);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.message);
        },
        complete: () => {
          console.log('Done');
          window.location.reload();
        }
      }
    )
  }



}
