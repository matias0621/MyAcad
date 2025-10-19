import { Routes } from '@angular/router';
import { Students } from './pages/Users/students/students';
import { Home } from './pages/home/home';
import { Teachers } from './pages/Users/teachers/teachers';
import { Managers } from './pages/Users/managers/managers';
import { Courses } from './pages/courses/courses';
import { Engineerings } from './pages/careers/engineerings/engineerings';
import { Technicals } from './pages/careers/technicals/technicals';

export const routes: Routes = [
    {path: '', component: Home},
    {path: 'students', component: Students},
    {path: 'teachers', component: Teachers},
    {path: 'managers', component: Managers},
    {path: 'engineerings', component: Engineerings},
    {path: 'technicals', component: Technicals},
    {path: 'courses', component: Courses}

];
