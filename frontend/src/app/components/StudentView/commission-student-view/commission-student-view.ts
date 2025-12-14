import { Component, OnInit } from '@angular/core';
import { CommissionService } from '../../../Services/Commission/commission-service';
import { ActivatedRoute } from '@angular/router';
import Commission from '../../../Models/Commission/commission';
import { ExamsService } from '../../../Services/Exams/exams-service';
import { Exams } from '../../../Models/Exam/Exam';
import { AuthService } from '../../../Services/Auth/auth-service';
import { SubjectsXStudentService } from '../../../Services/SubjectsXStudent/subjects-xstudent-service';
import Subjects from '../../../Models/Subjects/Subjects';
import { SubjectsXStudent } from '../../../Models/SubjectsXStudent/SubjectsXStudent';

@Component({
  selector: 'app-commission-student-view',
  imports: [],
  templateUrl: './commission-student-view.html',
  styleUrl: './commission-student-view.css',
})
export class CommissionStudentView implements OnInit {
  programName!: string;
  commissionList!: Commission[];
  listExams!:Exams[]
  listSubjects: SubjectsXStudent[] = [];

  constructor(
    private commissionService: CommissionService,
    private activatedRoute: ActivatedRoute,
    private auth:AuthService,
    private examsService:ExamsService,
    private subjectsXStudentService: SubjectsXStudentService
  ) {
    this.programName = this.activatedRoute.snapshot.params['name'];
  }

  ngOnInit(): void {
    this.getCommissionByProgramName()
    this.getExamByStudentId()
  }

  getCommissionByProgramName() {
    this.commissionService.getCommissionByStudentInfo(this.programName).subscribe({
      next: (res) => {
        console.log(res)
        this.commissionList = res;

        this.commissionList.forEach(c => c.subjects.forEach(s => this.getSubjectsByProgram(c.id,s.id)) )
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  getSubjectsByProgram(commissionId:number, subjectId:number){
    const token:any = this.auth.getDecodedToken()
    if (!token) return;

    this.subjectsXStudentService.getBySubjectIdStudentIdCommissionId(subjectId,token.id,commissionId).subscribe({
      next:(res)=>{
        if (!this.listSubjects.includes(res)){
          this.listSubjects.push(res)
        }
      },
      error: (err)=>{
        console.log(err);
      }
    })
  }

  getExamByStudentId(){
    const token:any = this.auth.getDecodedToken()
    if (!token) return;

    this.examsService.getExamsByStudents(token.id).subscribe({
      next: (res) => {
        this.listExams = res
      },
      error: (err) => {
        console.log(err)
      }
    })
  }
}
