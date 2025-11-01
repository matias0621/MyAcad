import { ActivatedRoute } from '@angular/router';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InscriptionToFinalExamService } from '../../Services/InscriptionToFinalExam/inscription-to-final-exam-service';
import { SubjectsService } from '../../Services/Subjects/subjects-service';
import { PostInscriptionToFinalExam } from '../../Models/InscriptionToFinalExam/InscriptionToFinalExam';

@Component({
  selector: 'app-inscription-to-exam-form-edit',
  imports: [ReactiveFormsModule],
  templateUrl: './inscription-to-exam-form-edit.html',
  styleUrl: './inscription-to-exam-form-edit.css'
})
export class InscriptionToExamFormEdit {
  form!: FormGroup;
  inscriptionDate!: FormControl;
  finalExamDate!: FormControl;
  subjectId: number | null = null;
  idInscription!:string

  constructor(
    public inscriptionToFinalExamService: InscriptionToFinalExamService,
    public subjectsServices: SubjectsService,
    public activatedRoute:ActivatedRoute
  ) {
    this.inscriptionDate = new FormControl('', [Validators.required]);
    this.finalExamDate = new FormControl('', [Validators.required]);

    this.idInscription = this.activatedRoute.snapshot.params['id']

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
    this.inscriptionToFinalExamService.getInscriptionById(parseInt(this.idInscription)).subscribe({
      next: (res) => {
        this.inscriptionDate.setValue(res.inscriptionDate)
        this.finalExamDate.setValue(res.finalExamDate)
        this.subjectId = res.subjects.id
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

    this.inscriptionToFinalExamService.putInscriptionToFinal(inscription,parseInt(this.idInscription)).subscribe({
      next: (res) => {
        alert('Se edito la inscripcion');
        this.getInscription()
        console.log(res);
        this.form.reset();
      },
      error: (err) => {
        alert('Hubo un error al cargar');
        console.log(err);
      },
    });
  }
}
