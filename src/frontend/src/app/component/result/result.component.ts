import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Course } from 'src/app/interface/course';
import { Result } from 'src/app/interface/result';
import { User } from 'src/app/interface/user';
import { CourseService } from 'src/app/service/course.service';
import { ResultService } from 'src/app/service/result.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent implements OnInit {

  listResult: Result[] = [];
  listUser: User[] = [];
  listCourse: Course[] = [];
  result: Result;
  listGrade : Array<number> = [1,2,3,4,5,6,7,8,9,10];

  constructor(private resultService: ResultService,
              private userService: UserService, 
              private courseService: CourseService) { }

  ngOnInit(): void {
    this.getListResult();
    this.getListCourse();
    this.getListUser();
  }
  public getResult(result: Result):void {
    this.result = result;
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

  public getListResult():void {
    this.resultService.getListResult().subscribe(
      {
        next: (resposne: Result[]) => {
          this.listResult = resposne;
          console.log(this.listResult);
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

  public addResult(addForm: NgForm): void {
    const form = document.getElementById('main-form-add')
    const button = document.createElement('button');
    button.type = 'reset';
    button.style.display = 'none';
    form.appendChild(button);
    document.getElementById('btn-close').click();
    this.resultService.addResult(addForm.value.userId,
                                      addForm.value.courseId,
                                      addForm.value.grade).subscribe(
      {
        next: (resposne: void) => {
          console.log(resposne);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.message);
          button.click();
        },
        complete: () => {
          console.log('Done');
          window.location.reload();
        }
      }
    )
  }

  public deleteResult(userId: number, courseId: number):void {
    document.getElementById('btn-no-delete').click();
    this.resultService.deleteResult(userId,courseId).subscribe(
      {
        next: (resposne: void) => {
          console.log(resposne);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.message);
        },
        complete: () => {
          console.log('Done');
          this.getListResult();
        }
      }
    )
  }

  public updateResult(updateForm: NgForm):void {
    document.getElementById('btn-edit-close').click();
    this.resultService.updateResult(updateForm.value.userId,
                                    updateForm.value.courseId,
                                    updateForm.value.grade).subscribe(
      {
        next: (response: void) => {
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

  public searchResult(input: string):void
  {
    console.log(input);
    const results: Result[] = [];
    console.log(results);
    for(const result of this.listResult) //loop for teacher
    {
      if(result?.user?.name.toLowerCase().indexOf(input.toLowerCase()) !== -1  //if user.name include input
      || result?.course?.name.toLowerCase().indexOf(input.toLowerCase()) !== -1)  //if email.email include input
      {
        results.push(result); //push student in result array
      }

    }
    this.listResult = results; //filter new list user when input match user.name or user.email
    if(results.length === 0 || !input) //if result empty or input empty
    {
      this.getListResult(); //reload list student
    }
  }

}
