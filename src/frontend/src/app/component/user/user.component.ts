import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Role } from 'src/app/interface/role';
import { User } from 'src/app/interface/user';
import { AuthService } from 'src/app/service/auth.service';
import { RoleService } from 'src/app/service/role.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  listUser: User[] = [];
  listRole: Role[] = [];
  user: User;
  userLogin: User;

  selectedFile: File;

  // constructor(private userService: UserService, 
  //             private roleService: RoleService) { }

  constructor(private userService: UserService, 
              private roleService: RoleService,
              private authService: AuthService) { }

  ngOnInit(): void {
    this.getListUser();
    this.getListRole();
    this.getUserLogin();
  }

  public fileSelected(event: any): void{
    this.selectedFile = event.target.files[0];
    console.log(this.selectedFile);
  }

  public getUser(user: User): void {
    this.user = user;
  }

  public getUserLogin(): void {
    this.userService.getUser(this.authService.getUserId()).subscribe(
      {
       next: (resposne: User) => {
           this.userLogin = resposne;
           console.log(this.userLogin);
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
  
  public getListUser():void {
      this.userService.getListUser().subscribe(
        {
          next: (response: User[]) => {
            this.listUser = response;
            console.log(this.listUser);
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

  public addUser(addForm: NgForm): void {
    document.getElementById('btn-close').click();
    const form = document.getElementById('main-form-add')
    const button = document.createElement('button');
    button.type = 'reset';
    button.style.display = 'none';
    form.appendChild(button);
    const formData = new FormData();
    const user = {name: addForm.value.name,
                  email: addForm.value.email,
                  password: addForm.value.password,
                  address: addForm.value.address,
                  enable: addForm.value.enable}
    if(this.selectedFile === null) {
      formData.append('user',JSON.stringify(user)); //Convert Object to JSON string
    } else {
      formData.append('user',JSON.stringify(user));
      formData.append('file',this.selectedFile);
    }
    this.userService.addUser(addForm.value.roleId,formData).subscribe(
      {
        next: (response: void) => {
          console.log(response);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.message);
          button.click();
        },
        complete: () => {
          console.log('done');
          window.location.reload();
        }
      }
    )
  }
  public deleteUser(userId: number): void {
    document.getElementById('btn-no-delete').click();
    this.userService.deleteUser(userId).subscribe(
      {
        next: (response: void) => {
          console.log(response);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.message);
        },
        complete: () => {
          console.log('Done');
          this.getListUser();
        }
      }
    )
  }

  public updateUser(updateForm: NgForm): void {
    document.getElementById('btn-edit-close').click();
    const formData = new FormData();
    formData.append('name',updateForm.value.name);
    formData.append('email',updateForm.value.email);
    formData.append('address',updateForm.value.address);
    formData.append('enable',updateForm.value.enable);
    formData.append('roleId',updateForm.value.roleId);
    if(this.selectedFile !== null) {
      formData.append('file',this.selectedFile);
    } 
    
    this.userService.updateUser(updateForm.value.id,this.authService.getUserId(),formData).subscribe(
      {
        next: (response: User) => {
          console.log(response);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.message);
        },
        complete: () => {
          console.log('Done');
          alert('Update successfully');
          window.location.reload();
        }
      }
    ) 
  }

  public searchUser(input: string):void
  {
    console.log(input);
    const result: User[] = [];
    console.log(result);
    for(const user of this.listUser) //loop of js
    {
      if(user?.name.toLowerCase().indexOf(input.toLowerCase()) !== -1  //if user.name include input
      || user?.email.toLowerCase().indexOf(input.toLowerCase()) !== -1)  //if email.email include input
      {
        result.push(user); //push student in result array
      }

    }
    this.listUser = result; //filter new list user when input match user.name or user.email
    if(result.length === 0 || !input) //if result empty or input empty
    {
      this.getListUser(); //reload list student
    }
  }

}
