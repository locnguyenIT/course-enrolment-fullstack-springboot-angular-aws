import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Category } from 'src/app/interface/category';
import { Course } from 'src/app/interface/course';
import { Teacher } from 'src/app/interface/teacher';
import { CategoryService } from 'src/app/service/category.service';
import { CourseService } from 'src/app/service/course.service';
import { TeacherService } from 'src/app/service/teacher.service';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css']
})
export class CourseComponent implements OnInit {

  listCourse: Course[] = [];
  listCategory: Category[] = [];
  listTeacher: Teacher[] = [];
  course: Course;
  selectedFile : File;

  constructor(private courseService : CourseService, 
              private categoryService: CategoryService, 
              private teacherService: TeacherService) { }

  ngOnInit(): void {
    
    this.getListCourse();
    this.getListCategory();
    this.getListTeacher();
  }

  public getCourse(course: Course): void {
    this.course = course;
  }
  public getListCategory(): void {
    this.categoryService.getListCategory().subscribe(
      {
        next: (respone: Category[]) => {
          this.listCategory = respone;
          console.log(this.listCategory);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.meesage);
        },
      }
    )
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
  
  public fileSelected(event: any): void{
    this.selectedFile = event.target.files[0];
    console.log(this.selectedFile);
  }

  public addCourse(addForm: NgForm):void {
    const form = document.getElementById('main-form-add')
    const button = document.createElement('button');
    button.type = 'reset';
    button.style.display = 'none';
    form.appendChild(button);
    document.getElementById('btn-close').click();
    const formData = new FormData();
    const course = {name: addForm.value.name,start_at: addForm.value.start_at,
                    end_at: addForm.value.end_at}
    if(this.selectedFile === null) {
      formData.append('course',JSON.stringify(course));
    
    } else {
      formData.append('course',JSON.stringify(course));
      formData.append('file',this.selectedFile);
    }
    this.courseService.addCourse(addForm.value.categoryId,addForm.value.teacherId,formData).subscribe(
      {
        next: (response: void) => {
          console.log(response);
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

  public deleteCourse(courseId : number): void {
    document.getElementById('btn-no-delete').click();
    this.courseService.deleteCourse(courseId).subscribe(
      {
        next: (response: void) => {
          console.log(response);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.message);
        },
        complete: () => {
          console.log('Done');
          this.getListCourse();
        }
      }
    )
  }

  public updateCourse(updateForm: NgForm): void {
    document.getElementById('btn-edit-close').click();
    const formData = new FormData();
    formData.append('name',updateForm.value.name);
    formData.append('start_at',updateForm.value.start_at);
    formData.append('end_at',updateForm.value.end_at);
    formData.append('categoryId',updateForm.value.categoryId);
    formData.append('teacherId',updateForm.value.teacherId);
    if(this.selectedFile !== null) {
      formData.append('file',this.selectedFile);
    } 
    this.courseService.updateCourse(updateForm.value.id,formData).subscribe(
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

  public searchCourse(input: string):void
  {
    console.log(input);
    const result: Course[] = [];
    console.log(result);
    for(const course of this.listCourse) //loop of js
    {
      if(course?.name.toLowerCase().indexOf(input.toLowerCase()) !== -1)  //if user.name include input  //if email.email include input
      {
        result.push(course); //push student in result array
      }

    }
    this.listCourse = result; //filter new list user when input match user.name or user.email
    if(result.length === 0 || !input) //if result empty or input empty
    {
      this.getListCourse(); //reload list student
    }
  }

  public searchCourseOfCategory(categoryId: string):void
  {
    if(categoryId !== "") {
      this.courseService.getListCourseOfCatefory(categoryId).subscribe(
        {
          next: (respose: Course[]) => {
            this.listCourse = respose;
          },
          error: (err : HttpErrorResponse) => {
            alert(err.error.message);
          }
        }
      )
    } else {
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
  }

  public searchCourseOfTeacher(teacherId: string):void
  {
    if(teacherId !== "") {
      this.courseService.getListCourseOfTeacher(teacherId).subscribe(
        {
          next: (respose: Course[]) => {
            this.listCourse = respose;
          },
          error: (err : HttpErrorResponse) => {
            alert(err.error.message);
          }
        }
      )
    } else {
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

  }

}
