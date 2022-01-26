import { TeacherService } from './../../service/teacher.service';
import { Teacher } from './../../interface/teacher';
import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-teacher',
  templateUrl: './teacher.component.html',
  styleUrls: ['./teacher.component.css']
})
export class TeacherComponent implements OnInit {

  listTeacher: Teacher[] = [];
  teacher: Teacher;
  selectedFile : File;

  constructor(private teacherService: TeacherService) { }

  ngOnInit(): void {
    this.getListTeacher();
  }

  public fileSelected(event: any): void{
    this.selectedFile = event.target.files[0];
    console.log(this.selectedFile);
  }

  public getTeacher(teacher: Teacher): void {
    this.teacher = teacher;
  }

  public getListTeacher(): void {
    this.teacherService.getListTeacher().subscribe(
      {
        next: (response: Teacher[]) => {
          this.listTeacher = response;
          console.log(this.listTeacher);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.message);
        }
      }
    )
  }

  public addTeacher(addForm: NgForm): void{
    //If error. create button reset to reset the form.
    const form = document.getElementById('main-form-add')
    const button = document.createElement('button');
    button.type = 'reset';
    button.style.display = 'none';
    form.appendChild(button);
    document.getElementById('btn-close').click();
    const formData = new FormData();
    formData.append('teacher',JSON.stringify(addForm.value)); //convert Object to JSON string
    if(this.selectedFile !== null) {
      formData.append('file',this.selectedFile);
    }

    this.teacherService.addTeacher(formData).subscribe(
      {
        next: (response: void) => {
          console.log(response);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.message);
          button.click();
        },
        complete: () => {
          alert('Done');
          window.location.reload();
        }
      }
    )
  }

  public deleteTeacher(teacherId : number): void {
    document.getElementById('btn-no-delete').click();
    this.teacherService.deleteTeacher(teacherId).subscribe(
      {
        next: (response: void) => {
          console.log(response);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.message);

        },
        complete: () => {
          console.log('Done');
          this.getListTeacher();
        }
      }
    )
  }

  public updateTeacher(updateForm: NgForm): void {
    document.getElementById('btn-edit-close').click();
    const formData = new FormData();
    if(this.selectedFile === null) {
      formData.append('name',updateForm.value.name); //Convert Object to JSON string
      formData.append('email',updateForm.value.email);
      formData.append('specialize',updateForm.value.specialize);
    } else {
      formData.append('name',(updateForm.value.name)); //Convert Object to JSON string
      formData.append('email',(updateForm.value.email));
      formData.append('specialize',updateForm.value.specialize);
      formData.append('file',this.selectedFile);
    }
    this.teacherService.updateTeacher(updateForm.value.id,formData).subscribe(
      {
        next: (response: void) => {
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

  public searchTeacher(input: string):void
  {
    console.log(input);
    const result: Teacher[] = [];
    console.log(result);
    for(const teacher of this.listTeacher) //loop for teacher
    {
      if(teacher?.name.toLowerCase().indexOf(input.toLowerCase()) !== -1  //if user.name include input
      || teacher?.email.toLowerCase().indexOf(input.toLowerCase()) !== -1)  //if email.email include input
      {
        result.push(teacher); //push student in result array
      }

    }
    this.listTeacher = result; //filter new list user when input match user.name or user.email
    if(result.length === 0 || !input) //if result empty or input empty
    {
      this.getListTeacher(); //reload list student
    }
  }
}
