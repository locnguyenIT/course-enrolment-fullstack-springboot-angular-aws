import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Course } from 'src/app/interface/course';
import { Enrolment } from 'src/app/interface/enrolment';
import { User } from 'src/app/interface/user';
import { AuthService } from 'src/app/service/auth.service';
import { CourseService } from 'src/app/service/course.service';
import { EnrolmentService } from 'src/app/service/enrolment.service';
import { UserService } from 'src/app/service/user.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-enrolment',
  templateUrl: './enrolment.component.html',
  styleUrls: ['./enrolment.component.css']
})
export class EnrolmentComponent implements OnInit {

  listEnrolment: Enrolment[] = [];
  listUser: User[] = [];
  user: User;
  listCourse: Course[] = [];
  enrolment: Enrolment;
  date: string;
  isUser: boolean = false;

  constructor(private enrolmentService: EnrolmentService,
              private userService: UserService,
              private courseService: CourseService,
              private authService: AuthService,
              private datePipe: DatePipe) { }

  ngOnInit(): void {
    this.getListEnrolment();
    this.getListCourse();
    this.getListUser();
    this.getUser();
  }
  
  public getListCourse(): void{
    this.courseService.getListCourse().subscribe(
      {
        next: (response: Course[]) =>{
          this.listCourse = response;
          console.log(this.listCourse);
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

  public getUser(): void {
     this.userService.getUser(this.authService.getUserId()).subscribe(
       {
        next: (resposne: User) => {
            this.user = resposne;
            console.log(this.user);
            if(this.user?.role?.name == 'USER') {
              this.isUser = true;
            }
            else {
              this.isUser = false;
            }
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

  public getListEnrolment(): void {
    this.enrolmentService.getListEnrolment().subscribe(
      {
        next: (response: Enrolment[]) => {
          this.listEnrolment = response;
          console.log(this.listEnrolment);
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

  public getEnrolment(enrolment: Enrolment):void {
    this.enrolment = enrolment;
  }

  public addEnrolment(addForm: NgForm): void {
    //create button reset the form when have a error
    const form = document.getElementById('main-form-add')
    const button = document.createElement('button');
    button.type = 'reset';
    button.style.display = 'none';
    form.appendChild(button);
    document.getElementById('btn-close').click();
    this.date = this.datePipe.transform(new Date(), 'yyyy-MM-ddTHH:mm:ss');
    this.enrolmentService.addEnrolment(addForm.value.userId,
                                      addForm.value.courseId,
                                      this.date).subscribe(
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

  public deleteEnrolment(userId: number, courseId: number):void {
    document.getElementById('btn-no-delete').click();
    this.enrolmentService.deleteEnrolment(userId,courseId).subscribe(
      {
        next: (resposne: void) => {
          console.log(resposne);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.message);
        },
        complete: () => {
          console.log('Done');
          this.getListEnrolment();
        }
      }
    )
  }

  public searchEnrolment(input: string):void
  {
    console.log(input);
    const result: Enrolment[] = [];
    console.log(result);
    for(const enrolment of this.listEnrolment) //loop for teacher
    {
      if(enrolment?.user?.name.toLowerCase().indexOf(input.toLowerCase()) !== -1  //if user.name include input
      || enrolment?.course?.name.toLowerCase().indexOf(input.toLowerCase()) !== -1)  //if email.email include input
      {
        result.push(enrolment); //push student in result array
      }

    }
    this.listEnrolment = result; //filter new list user when input match user.name or user.email
    if(result.length === 0 || !input) //if result empty or input empty
    {
      this.getListEnrolment(); //reload list student
    }
  }

}
