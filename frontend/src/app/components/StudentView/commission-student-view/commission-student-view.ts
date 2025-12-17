import { CourseEvaluation, PostCourseEvaluation } from './../../../Models/CourseEvaluation/CourseEvaluation';
import { SettingService } from './../../../Services/setting-service';
import { Component, HOST_TAG_NAME, OnInit } from '@angular/core';
import { CommissionService } from '../../../Services/Commission/commission-service';
import { ActivatedRoute } from '@angular/router';
import Commission from '../../../Models/Commission/commission';
import { ExamsService } from '../../../Services/Exams/exams-service';
import { Exams } from '../../../Models/Exam/Exam';
import { AuthService } from '../../../Services/Auth/auth-service';
import { SubjectsXStudentService } from '../../../Services/SubjectsXStudent/subjects-xstudent-service';
import Subjects from '../../../Models/Subjects/Subjects';
import { SubjectsXStudent } from '../../../Models/SubjectsXStudent/SubjectsXStudent';
import Teacher from '../../../Models/Users/Teachers';
import { UserService } from '../../../Services/Users/user-service';
import { NotificationService } from '../../../Services/notification/notification.service';
import { CourseEvaluationService } from '../../../Services/CourseEvaluation/course-evaluation-service';
import { FormControl, FormGroup, Validators, ɵInternalFormsSharedModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-commission-student-view',
  imports: [ɵInternalFormsSharedModule, ReactiveFormsModule],
  templateUrl: './commission-student-view.html',
  styleUrl: './commission-student-view.css',
})
export class CommissionStudentView implements OnInit {
  programName!: string;
  commissionList!: Commission[];
  listExams!:Exams[]
  listSubjects: SubjectsXStudent[] = [];
  showEvaluation:boolean = false
  subject!:Subjects | null
  commission!:Commission | null
  teacher!:Teacher | null
  evaluatedSubjects: Set<string> = new Set()

  formReview!:FormGroup
  feedbackInput!: FormControl
  teacherFeedbackInput!: FormControl
  contentFeedbackInput!: FormControl
  suggestionInput!: FormControl

  constructor(
    private commissionService: CommissionService,
    private activatedRoute: ActivatedRoute,
    private auth:AuthService,
    private examsService:ExamsService,
    private subjectsXStudentService: SubjectsXStudentService,
    public settingService:SettingService,
    public teacherService:UserService,
    public notificationService:NotificationService,
    public courseEvaluationService:CourseEvaluationService
  ) {
    this.programName = this.activatedRoute.snapshot.params['name'];

    this.feedbackInput = new FormControl("")
    this.teacherFeedbackInput = new FormControl("", Validators.required)
    this.contentFeedbackInput = new FormControl("", Validators.required)
    this.suggestionInput = new FormControl("")

    this.formReview = new FormGroup({
      feedback: this.feedbackInput,
      teacherFeedback: this.teacherFeedbackInput,
      contentFeedback: this.contentFeedbackInput,
      suggestion: this.suggestionInput
    })
  }

  ngOnInit(): void {
    this.loadEvaluationSetting()
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
          this.checkIfEvaluated(subjectId, commissionId)
        }
      },
      error: (err)=>{
        console.log(err);
      }
    })
  }

  checkIfEvaluated(subjectId: number, commissionId: number) {
    const token:any = this.auth.getDecodedToken()
    if (!token) return;
    
    const key = `evaluation-${token.id}-${subjectId}-${commissionId}`
    const evaluated = localStorage.getItem(key)
    if (evaluated === 'true') {
      const setKey = `${subjectId}-${commissionId}`
      this.evaluatedSubjects.add(setKey)
    }
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

  setDataForEvaluetion(com:Commission,sub:Subjects){
    this.commission = com
    this.subject = sub

    console.log(com)
    console.log(sub)

    this.getTeacherByCommissionAndSubject(this.commission, this.subject)
  }

  loadEvaluationSetting() {
    this.settingService.isCourseEvaluationEnabled().subscribe(value => {
      this.showEvaluation = value;
      console.log("AAAAAAAAAAAAAAAAAAAAAA",value)
    });
  }

  getTeacherByCommissionAndSubject(com:Commission, sub:Subjects){
    this.teacherService.getTeacherByCommissionAndSubject(com.id,sub.id).subscribe({
      next: (res) => {
        this.teacher = res
        console.log(res)
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  capitalizeFirst(text: string): string {
    if (!text) return text
    return text.charAt(0).toUpperCase() + text.slice(1)
  }

  hasEvaluated(subjectId: number, commissionId: number): boolean {
    const key = `${subjectId}-${commissionId}`
    return this.evaluatedSubjects.has(key)
  }

  createCourseEvaluetion(){
    if(this.commission === null || this.teacher === null || this.subject === null){
      this.notificationService.error("Hubo un error", true)
      return
    }

    const teacherFeedback = this.teacherFeedbackInput.value || ''
    const contentFeedback = this.contentFeedbackInput.value || ''
    const suggestion = this.suggestionInput.value || ''
    
    const combinedFeedback = `DOCENTE: ${teacherFeedback}\n\nCONTENIDOS: ${contentFeedback}\n\nSUGERENCIAS: ${suggestion}`

    const evaluation:PostCourseEvaluation = {
      feedback: combinedFeedback,
      subjectId: this.subject.id,
      teacherId: this.teacher.id,
      teacherFeedback: teacherFeedback,
      contentFeedback: contentFeedback,
      suggestion: suggestion
    }

    this.courseEvaluationService.create(evaluation).subscribe({
      next: () => {
        const token:any = this.auth.getDecodedToken()
        const key = `${this.subject!.id}-${this.commission!.id}`
        this.evaluatedSubjects.add(key)
        
        if (token) {
          const storageKey = `evaluation-${token.id}-${this.subject!.id}-${this.commission!.id}`
          localStorage.setItem(storageKey, 'true')
        }
        
        this.notificationService.success("Tu encuesta se subió correctamente")
        this.formReview.reset()
      },
      error: (err) => {
        this.notificationService.error("Hubo un error", true)
        console.log(err)
      }
    })
  }
}
