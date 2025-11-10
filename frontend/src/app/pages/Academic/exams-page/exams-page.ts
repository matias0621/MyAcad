import { NotificationService } from './../../../Services/notification/notification.service';
import { Component, OnInit } from '@angular/core';
import { Exams, ExamsPost } from '../../../Models/Exam/Exam';
import { ExamsService } from '../../../Services/Exams/exams-service';
import { SubjectsService } from '../../../Services/Subjects/subjects-service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CareerService } from '../../../Services/CareerService/career-service';
import { ProgramService } from '../../../Services/program-service';
import Program from '../../../Models/Program/Program';
import Subjects from '../../../Models/Subjects/Subjects';


@Component({
  selector: 'app-exams-page',
  imports: [ReactiveFormsModule],
  templateUrl: './exams-page.html',
  styleUrl: './exams-page.css',
})
export class ExamsPage implements OnInit {
  listExams: Exams[] = [];
  listCareer: Program[] = [];
  listSubjects: Subjects[] = [];
  form!: FormGroup;
  score!: FormControl;
  legajo!: FormControl;
  examType!: FormControl;
  careerName!: FormControl;
  subjects!: FormControl;
  selectedExam?: Exams;

  idSubjects!: number 
  examId!:number
  // Paginación
  totalPages: number = 0;
  currentPage: number = 0;

  constructor(
    private examsService: ExamsService,
    private subjectsService: SubjectsService,
    private notificationService: NotificationService,
    private programService: ProgramService,
    private router: Router
  ) {
    this.legajo = new FormControl('', [Validators.required]);
    this.examType = new FormControl('', [Validators.required]);
    this.score = new FormControl('', [Validators.required]);
    this.careerName = new FormControl('');
    this.subjects = new FormControl('', [Validators.required]);

    this.form = new FormGroup({
      legajo: this.legajo,
      score: this.score,
      careerName: this.careerName,
      subjects: this.subjects,
      examType: this.examType,
    });
  }

  ngOnInit(): void {
    this.getAllExam();
    this.getAllCarrer();

    // Cuando cambia la carrera seleccionada:
    this.careerName.valueChanges.subscribe((careerName: string) => {
      if (careerName) {
        this.getSubjectsByNameProgram(careerName);
      } else {
        this.listSubjects = []; // limpiamos las materias si no hay carrera seleccionada
      }
    });
  }

  getAllCarrer() {
    this.programService.getPrograms().subscribe({
      next: (res) => {
        this.listCareer = res;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }


  getAllExam(page: number = 0, size: number = 10) {
    this.examsService.getExamsPaginated(page, size).subscribe({
      next: (res) => {
        this.listExams = res.content;
        this.currentPage = res.number;
        this.totalPages = res.totalPages
      },
      error: (err) => {
        console.log(err)
      }
    });
  }

  getSubjectsByNameProgram(careerName: string) {
    this.subjectsService.getByProgram(careerName).subscribe({
      next: (res) => {
        this.listSubjects = res;
      },
      error: (err) => {
        console.error(err);
        this.listSubjects = [];
      },
    });
  }

  modifyExam(exam: Exams) {
    this.score.setValue(exam.score);
    this.legajo.setValue(exam.student.legajo);
    this.examType.setValue(exam.examType);
    this.careerName.setValue(exam.subject.program);
    this.subjects.setValue(exam.subject.id);
  }

  deleteExam(examId: number) {
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
  postExam() {
    if (this.form.invalid) {
      this.notificationService.warning(
        'Formulario inválido. Por favor, complete todos los campos correctamente.'
      );
      this.form.markAllAsTouched();
      return;
    }

    const examLoad: ExamsPost = {
      score: this.score.value,
      examType: this.examType.value,
      legajoStudent: this.legajo.value,
      subjectId: this.subjects.value,
    };

    this.examsService.postExam(examLoad).subscribe({
      next: (data) => {
        this.notificationService.success('Examen cdxagregado exitosamente');
        this.getAllExam();
        this.form.reset();
      },
      error: (error) => {
        this.notificationService.error(error.error, true);
        console.error(error);
      },
    });
  }

  putExam() {
    if (this.form.invalid) {
      this.notificationService.warning(
        'Formulario inválido. Por favor, complete todos los campos correctamente.'
      );
      this.form.markAllAsTouched();
      return;
    }

    const examLoad: ExamsPost = {
      score: this.score.value,
      examType: this.examType.value,
      legajoStudent: this.legajo.value,
      subjectId: this.subjects.value,
    };

    this.examsService.putExam(this.subjects.value, examLoad).subscribe({
      next: (data) => {
        this.notificationService.success('Examen actualizado exitosamente');
        this.form.reset();
      },
      error: (error) => {
        this.notificationService.error(error.error, true);
        console.error(error);
      },
    });
  }
}
