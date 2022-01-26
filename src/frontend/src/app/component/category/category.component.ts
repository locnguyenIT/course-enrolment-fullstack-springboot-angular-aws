import { CategoryService } from './../../service/category.service';
import { Component, OnInit } from '@angular/core';
import { Category } from 'src/app/interface/category';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {

  public listCategory: Category[] = [];
  public category: Category;
  public errorMessage: string;

  constructor(private categoryService: CategoryService) { }

  ngOnInit(): void {
    this.getListCategory();
  }

  public getCategory(category: Category): void {
    this.category = category;
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

  public addCategory(addForm : NgForm): void {
    document.getElementById('btn-close').click();
    this.categoryService.addCategory(addForm.value).subscribe(
      {
        next: (respone: void) => {
          console.log(respone);
        },
        error: (err: HttpErrorResponse) => {
          this.errorMessage = err.error.message;
          alert(this.errorMessage);
          addForm.reset();
        },
        complete: () => {
          window.location.reload();
        }
      }
    )
  }

  public deleteCategory(categoryId: number): void{
    document.getElementById('btn-no-delete').click();
    this.categoryService.deleteCategory(categoryId).subscribe(
      {
        next: (response: void) => {
          console.log(response);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.message);
        },
        complete: () => {
          this.getListCategory();
        }
      }
    )
  }

  public updateCategory(category: Category): void {
    document.getElementById('btn-edit-close').click();
    this.categoryService.updateCategory(category).subscribe(
      {
        next: (response: void) => {
          console.log(response);
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error.message);
          window.location.reload();
        },
        complete: () => {
          alert('Update successfully');
          window.location.reload();
        }
      }
    )
  }

  public searchCategory(input: string):void
  {
    console.log(input);
    const result: Category[] = [];
    console.log(result);
    for(const category of this.listCategory) //loop for teacher
    {
      if(category.name.toLowerCase().indexOf(input.toLowerCase()) !== -1)  //if user.name include input
      {
        result.push(category); //push student in result array
      }

    }
    this.listCategory = result; //filter new list user when input match user.name or user.email
    if(result.length === 0 || !input) //if result empty or input empty
    {
      this.getListCategory(); //reload list student
    }
  }

}
