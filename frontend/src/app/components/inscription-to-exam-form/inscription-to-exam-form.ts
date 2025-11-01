import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InscriptionToFinalExamService } from '../../Services/InscriptionToFinalExam/inscription-to-final-exam-service';
import { SubjectsService } from '../../Services/Subjects/subjects-service';
import { PostInscriptionToFinalExam } from '../../Models/InscriptionToFinalExam/InscriptionToFinalExam';
import { InscriptionToExamList } from '../inscription-to-exam-list/inscription-to-exam-list';
import { Router } from '@angular/router';

@Component({
  selector: 'app-inscription-to-exam-form',
  imports: [ReactiveFormsModule],
  templateUrl: './inscription-to-exam-form.html',
  styleUrl: './inscription-to-exam-form.css',
})
export class InscriptionToExamForm implements OnInit {
  form!: FormGroup;
  inscriptionDate!: FormControl;
  finalExamDate!: FormControl;
  subjectId: number | null = null;

  constructor(
    public inscriptionToFinalExamService: InscriptionToFinalExamService,
    public subjectsServices: SubjectsService,
    public router:Router
  ) {
    this.inscriptionDate = new FormControl('', [Validators.required]);
    this.finalExamDate = new FormControl('', [Validators.required]);

    this.form = new FormGroup({
      inscriptionDate: this.inscriptionDate,
      finalExamDate: this.finalExamDate,
    });
  }

  ngOnInit(): void {
    this.getInscription();
    this.getSubjects();
  }

  addToSubject(idSubject: number) {
    this.subjectId = idSubject;
    alert('Se asigno corectamente la materia a la inscripcion');
  }

  getInscription() {
    this.inscriptionToFinalExamService.getAllInscription().subscribe({
      next: (res) => {
        this.inscriptionToFinalExamService.inscriptionList = res;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  getSubjects() {
    this.subjectsServices.getAllSubject().subscribe({
      next: (res) => {
        this.subjectsServices.listSubject = [...res];
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  OnSubmit() {
    if (this.subjectId === null) {
      alert('Añada de que materia es el examen');
      return;
    }

    const formatDate = (dateStr: string) => {
      const date = new Date(dateStr);
      const dd = String(date.getDate()).padStart(2, '0');
      const mm = String(date.getMonth() + 1).padStart(2, '0');
      const yyyy = date.getFullYear();
      const hh = String(date.getHours()).padStart(2, '0');
      const min = String(date.getMinutes()).padStart(2, '0');
      return `${dd}/${mm}/${yyyy} ${hh}:${min}`;
    };

    const inscription: PostInscriptionToFinalExam = {
      inscriptionDate: formatDate(this.inscriptionDate.value),
      finalExamDate: formatDate(this.finalExamDate.value),
      subjectsId: this.subjectId,
    };

    console.log(inscription);

    this.inscriptionToFinalExamService.postInscriptionToFinal(inscription).subscribe({
      next: (res) => {
        alert('Se añadi la inscripcion');
        this.getInscription();
        console.log(res);
        this.form.reset();
      },
      error: (err) => {
        alert('Hubo un error al cargar');
        console.log(err);
      },
    });
  }

  updateInscription(id:number){
    this.router.navigate(["/inscriptionToFinalExam/", id])
  }
}
