import { Routes } from '@angular/router';
import { Students } from './pages/Users/students/students';
import { Home } from './pages/home/home';
import { Teachers } from './pages/Users/teachers/teachers';
import { Managers } from './pages/Users/managers/managers';
import { Courses } from './pages/Programs/courses/courses';
import { Engineerings } from './pages/Programs/engineerings/engineerings';
import { Technicals } from './pages/Programs/technicals/technicals';
import { ProgramsEditForm } from './components/Programs/programs-edit-form/programs-edit-form';

import { Login } from './pages/login/login';
import { AuthGuard } from './Services/Auth/auth-guard';


import { Commissions } from './pages/Academic/commissions/commissions';
import { InscriptionFinalExams } from './pages/inscription-final-exams/inscription-final-exams';
import { InscriptionToExamFormEdit } from './components/inscription-to-exam-form-edit/inscription-to-exam-form-edit';
import { InscriptionToExamList } from './components/inscription-to-exam-list/inscription-to-exam-list';
import { InscriptionCommision } from './pages/inscription-commision/inscription-commision';
import { SubjectsPage } from './pages/Academic/subjects-page/subjects-page';
import { CommissionStudentView } from './components/StudentView/commission-student-view/commission-student-view';
import { ExamsPage } from './pages/Academic/exams-page/exams-page';
import { ExamsFormEdit } from './components/Academic/exams-form-edit/exams-form-edit';
import { ExamsForm } from './components/Academic/exams-form/exams-form';
import { StudentRegisterCommission } from './components/StudentView/student-register-commission/student-register-commission';





export const routes: Routes = [
    { path: 'auth/login', component: Login },

    { path: '', component: Home, canActivate: [AuthGuard] },
    { path: 'students', component: Students, canActivate: [AuthGuard] },
    { path: 'teachers', component: Teachers, canActivate: [AuthGuard] },
    { path: 'managers', component: Managers, canActivate: [AuthGuard] },

    { path: 'subject', component: SubjectsPage, canActivate: [AuthGuard] },
    { path: 'subject/:id', component: SubjectsPage, canActivate: [AuthGuard] },
    { path: 'commissions', component: Commissions, canActivate: [AuthGuard] },


    { path: 'exams', component:ExamsPage, canActivate: [AuthGuard] },
    { path: 'create-exam', component:ExamsForm, canActivate: [AuthGuard] },
    { path: 'exams/:id', component:ExamsFormEdit, canActivate: [AuthGuard] },


    { path: 'inscriptionToFinalExam', component: InscriptionFinalExams, canActivate: [AuthGuard] },
    { path: 'inscriptionToFinalExam/:id', component: InscriptionToExamFormEdit, canActivate: [AuthGuard] },

    { path: 'commissionForProgramName/:name', component: CommissionStudentView, canActivate: [AuthGuard] },

    { path: 'student-register-commission/:name', component: StudentRegisterCommission, canActivate: [AuthGuard] },

    { path: 'register-to-final-exam', component: InscriptionToExamList, canActivate:[AuthGuard] },

    { path: 'register-student-to-commission', component:InscriptionCommision },

    { path: 'engineerings', component: Engineerings, canActivate: [AuthGuard] },
    { path: 'technicals', component: Technicals, canActivate: [AuthGuard] },
    { path: 'courses', component: Courses, canActivate: [AuthGuard] },
    { path: 'programs-edit-form/:id', component: ProgramsEditForm, canActivate: [AuthGuard] }

];
