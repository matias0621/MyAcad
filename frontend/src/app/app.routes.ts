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
import { StudentRegisterCommission } from './components/StudentView/student-register-commission/student-register-commission';
import { CommissionsTeacherView } from './components/Teacher/commissions-teacher-view/commissions-teacher-view';
import { TeacherExamsView } from './components/Teacher/teacher-exams-view/teacher-exams-view';
import { TeacherGradesView } from './components/Teacher/teacher-grades-view/teacher-grades-view';
import { SubjectsTeacherView } from './components/Teacher/subjects-teacher-view/subjects-teacher-view';
import { TeacherFinalExamsView } from './components/Teacher/teacher-final-exams-view/teacher-final-exams-view';

import { ShowCareerForRegister } from './components/show-career-for-register/show-career-for-register';
import { SelectTypeRegisterPage } from './pages/select-type-register-page/select-type-register-page';
import { ShowCareerForCommission } from './components/show-career-for-commission/show-career-for-commission';
import { RegisterStudentCareer } from './components/register-student-career/register-student-career';
import { RegisterToFinalExam } from './components/StudentView/register-to-final-exam/register-to-final-exam';
import { StudentTicket } from './pages/student-ticket/student-ticket';
import { Certificates } from './pages/certificates/certificates';
import { InscriptionCommisionPage } from './pages/inscription-commision-page/inscription-commision-page';
import { RegisterCommission } from './components/StudentView/register-commission/register-commission';






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

    { path: 'inscriptionToFinalExam', component: InscriptionFinalExams, canActivate: [AuthGuard] },
    { path: 'inscriptionToFinalExam/:id', component: InscriptionToExamFormEdit, canActivate: [AuthGuard] },

    { path: 'inscriptionToCommission', component: InscriptionCommisionPage, canActivate: [AuthGuard] },

    { path: 'register-student-commission', component: RegisterCommission, canActivate: [AuthGuard] },
    
    { path: 'commissionForProgramName/:name', component: CommissionStudentView, canActivate: [AuthGuard] },

    { path: 'student-register-commission/:name', component: StudentRegisterCommission, canActivate: [AuthGuard] },

    { path: 'register-to-final-exam', component: InscriptionToExamList, canActivate:[AuthGuard] },

    { path: 'register-student-to-commission/:name', component:InscriptionCommision, canActivate:[AuthGuard] },

    { path: 'select-type-register', component:SelectTypeRegisterPage, canActivate:[AuthGuard] },

    { path: 'student-ticket', component: StudentTicket, canActivate: [AuthGuard] },
    { path: 'certificates', component: Certificates, canActivate: [AuthGuard] },

    { path: 'show-career-for-register', component:ShowCareerForRegister, canActivate:[AuthGuard] },
    { path: 'show-career-for-commission', component:ShowCareerForCommission, canActivate:[AuthGuard]},
    { path: 'register-student-career/:name', component:RegisterStudentCareer, canActivate:[AuthGuard] },
    { path: 'register-student-to-final-exam', component:RegisterToFinalExam, canActivate: [AuthGuard] },

    { path: 'engineerings', component: Engineerings, canActivate: [AuthGuard] },
    { path: 'technicals', component: Technicals, canActivate: [AuthGuard] },
    { path: 'courses', component: Courses, canActivate: [AuthGuard] },
    { path: 'programs-edit-form/:id', component: ProgramsEditForm, canActivate: [AuthGuard] },

    // Rutas Teacher
    { path: 'teacher/program/:programId/commissions', component: CommissionsTeacherView, canActivate: [AuthGuard] },
    { path: 'teacher/program/:programId/subjects', component: SubjectsTeacherView, canActivate: [AuthGuard] },
    { path: 'teacher/exams', component: TeacherExamsView, canActivate: [AuthGuard] },
    { path: 'teacher/grades', component: TeacherGradesView, canActivate: [AuthGuard] },
    { path: 'teacher/final-exams', component: TeacherFinalExamsView, canActivate: [AuthGuard] }

];
