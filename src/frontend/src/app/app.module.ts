import { TeacherService } from './service/teacher.service';
import { CategoryService } from './service/category.service';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule, routingComponent } from './app-routing.module';
import { AppComponent } from './app.component';
import { CourseService } from './service/course.service';
import { UserService } from './service/user.service';
import { EnrolmentService } from './service/enrolment.service';
import { DatePipe } from '@angular/common';
import { ResultService } from './service/result.service';
import { AuthService } from './service/auth.service';
import { HttpInterceptorProviders } from './interceptor/custom.interceptor';
import { PasswordService } from './service/password.service';
import { CustomGuard } from './guard/custom.guard';

@NgModule({
  declarations: [
    AppComponent,
    routingComponent,
  ],
  imports: [
    BrowserModule,    
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
   
  ],
  providers: [CategoryService,TeacherService,CourseService,UserService,EnrolmentService,ResultService,
               PasswordService,AuthService,DatePipe, CustomGuard, HttpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }
