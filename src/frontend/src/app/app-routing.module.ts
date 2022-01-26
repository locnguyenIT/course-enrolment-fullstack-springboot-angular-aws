import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CategoryComponent } from './component/category/category.component';
import { CourseComponent } from './component/course/course.component';
import { EnrolmentComponent } from './component/enrolment/enrolment.component';
import { ForgotPasswordComponent } from './component/forgot-password/forgot-password.component';
import { LoginComponent } from './component/login/login.component';
import { MenuComponent } from './component/menu/menu.component';
import { ResetPasswordComponent } from './component/reset-password/reset-password.component';
import { ResultComponent } from './component/result/result.component';
import { TeacherComponent } from './component/teacher/teacher.component';
import { UserProfileComponent } from './component/user-profile/user-profile.component';
import { UserComponent } from './component/user/user.component';
import { CustomGuard } from './guard/custom.guard';

const routes: Routes = [
{path: '',   redirectTo: 'login', pathMatch: 'full' }, // router level 1: /''
{path: 'login',component: LoginComponent },
{path: 'forgot-password',component: ForgotPasswordComponent },
{path: 'reset-password',component: ResetPasswordComponent},
{path: 'menu',component: MenuComponent, canActivateChild: [CustomGuard],
  children:[
    {path: '',   redirectTo: 'course', pathMatch: 'full'},
    {path: 'category',component: CategoryComponent},
    {path: 'teacher',component:TeacherComponent},
    {path: 'course',component:CourseComponent},
    {path: 'user',component:UserComponent},
    {path: 'enrolment',component:EnrolmentComponent},
    {path: 'result',component:ResultComponent},
    {path: 'user-profile',component:UserProfileComponent}

]}];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const routingComponent = [MenuComponent,CategoryComponent,TeacherComponent,CourseComponent,UserComponent,EnrolmentComponent,
                                ResultComponent, UserProfileComponent, LoginComponent, ForgotPasswordComponent, ResetPasswordComponent]
