import { Routes } from '@angular/router';
import { Students } from './pages/Users/students/students';
import { Home } from './pages/home/home';
import { Teachers } from './pages/Users/teachers/teachers';
import { Managers } from './pages/Users/managers/managers';

export const routes: Routes = [
    {path: '', component: Home},
    {path: 'students', component: Students},
    {path: 'teachers', component: Teachers},
    {path: 'managers', component: Managers},
];
