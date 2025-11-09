import { NotificationService } from './../../../Services/notification/notification.service';
import { Component, OnInit } from '@angular/core';
import { Exams, ExamsPost } from '../../../Models/Exam/Exam';
import { ExamsService } from '../../../Services/Exams/exams-service';
import { SubjectsService } from '../../../Services/Subjects/subjects-service';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-exams-page',
  imports: [],
  templateUrl: './exams-page.html',
  styleUrl: './exams-page.css',
})
export class ExamsPage implements OnInit {
  listExams: Exams[] = [];
  form!: FormGroup;
  score!: FormControl;
  legajo!: FormControl;
  examType!: FormControl;
  selectedExam ?: Exams;

  idSubjects!: number 
  examId!:number

  constructor(
    private examsService: ExamsService,
    private subjectsService: SubjectsService,
    private notificationService: NotificationService,
    private router:Router
  ) {}

  ngOnInit(): void {
    this.getAllExam()
  }

  getAllExam(){
    this.examsService.getAllExams().subscribe({
      next: (res) => {
        this.listExams = res;
        console.log(res)
        console.log(this.listExams)
      },
      error: (err) => {
        console.log(err)
      }
    });
  }

  addExam(){
    this.router.navigate(['/create-exam'])
  }

  modifyExam(exam:Exams){
    this.router.navigate(['/exams/', exam.id])
  }

  
  deleteExam(examId:number){
    this.notificationService
      .confirm(
        '¿Estás seguro de que deseas eliminar esta comisión?',
        'Confirmar eliminación',
        'Eliminar',
        'Cancelar'
      )
      .then((confirmed) => {
        if (confirmed) {
          this.examsService.deleteExam(examId).subscribe({
            next: (data) => {
              this.notificationService.success('Examen eliminada exitosamente');
              this.getAllExam();
            },
            error: (error) => {
              this.notificationService.error(
                'Error al eliminar el examen. Por favor, intenta nuevamente',
                true
              );
            },
          });
        }
      });
  }

  addSubjectsToExam(id: number) {
    this.idSubjects = id;
    this.notificationService.info('Se añadió esta materia al examen');
  }

  cleanForm() {
    this.form.reset();
  }
}
