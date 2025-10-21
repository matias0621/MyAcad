import { Routes } from '@angular/router';
import { Students } from './pages/Users/students/students';
import { Home } from './pages/home/home';
import { Teachers } from './pages/Users/teachers/teachers';
import { Managers } from './pages/Users/managers/managers';
import { SubjectForm } from './components/subject-form/subject-form';

export const routes: Routes = [
    {path: '', component: Home},
    {path: 'students', component: Students},
    {path: 'teachers', component: Teachers},
    {path: 'managers', component: Managers},
    {path: 'subject', component:SubjectForm}
];
