import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SubjectsService } from '../../../Services/Subjects/subjects-service';
import { ActivatedRoute, Router } from '@angular/router';
import Subjects from '../../../Models/Subjects/Subjects';
import { NotificationService } from '../../../Services/notification/notification.service';

@Component({
  selector: 'app-subject-form-edit',
  imports: [ReactiveFormsModule],
  templateUrl: './subject-form-edit.html',
  styleUrl: './subject-form-edit.css',
})
export class SubjectFormEdit implements OnInit {

  form!:FormGroup
  name!:FormControl
  description!:FormControl
  semesters!:FormControl
  subjectActive!:FormControl
  academicStatus!: FormControl;
  id!:string
  modalText = 'Agregar correlativas';
  listOfPrerequisite: Subjects[] = [];
  listOfSubjects: Subjects[] = [];
  selected = false;


  constructor(public subjectService:SubjectsService, 
    private activatedRouter:ActivatedRoute, 
    private router:Router,
    private notificationService: NotificationService
  ){
    this.name = new FormControl("", [Validators.required, Validators.maxLength(30)])
    this.description = new FormControl("", [Validators.required, Validators.maxLength(300)])
    this.semesters = new FormControl("", [Validators.required, Validators.min(1), Validators.maxLength(50)])
    this.academicStatus = new FormControl('', [Validators.required]);
    this.subjectActive = new FormControl("")
    this.id = this.activatedRouter.snapshot.params['id']

    this.form = new FormGroup({
      name: this.name,
      description: this.description,
      semesters: this.semesters,
      subjectActive: this.subjectActive,
      academicStatus: this.academicStatus,
    });
  }

  ngOnInit(): void {
    this.subjectService.getSubjectById(this.id).subscribe({
      next: (res) => {
        this.name.setValue(res.name);
        this.description.setValue(res.description);
        this.semesters.setValue(res.semesters);
        this.subjectActive.setValue(res.subjectActive);
        if (res.prerequisites != undefined) this.listOfPrerequisite = res.prerequisites;

        console.log(res.prerequisites);
        console.log(this.listOfPrerequisite);

        this.getSubjectsBySemesterLessThan(res.semesters);
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  addSubjects(subjects: Subjects) {
    if (!this.listOfPrerequisite.some((s) => s.id === subjects.id)) {
      this.listOfPrerequisite.push(subjects);
    }
  }

  deleteSubjectsOfPrerequisite(subjects: Subjects) {
    this.listOfPrerequisite = this.listOfPrerequisite.filter((s) => s.id !== subjects.id);
  }

  isSelected(subject: Subjects): boolean {
    return this.listOfPrerequisite.some((s) => s.id === subject.id);
  }

  getSubjectsBySemesterLessThan(semester: number) {
    this.subjectService.getAllSubjectWithSemesterLessThan(semester).subscribe({
      next: (res) => {
        this.listOfSubjects = res.filter((s) => s.id !== parseInt(this.id));
        this.listOfSubjects.sort((a, b) => a.semesters - b.semesters);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }


  OnSubmit() {
    if (this.form.invalid) {
      this.notificationService.warning("Complete todos los campos de la materia para subirla", true);
      return;
    }

    const subjectUpdate: Subjects = {
      id: parseInt(this.id),
      name: this.name.value,
      description: this.description.value,
      semesters: this.semesters.value,
      academicStatus: this.academicStatus.value,
      subjectActive: this.subjectActive.value,
      prerequisites: this.listOfPrerequisite,
    };
    this.subjectService.putSubject(subjectUpdate).subscribe({
      next: (res) => {
        console.log;
        this.notificationService.success("Se subió correctamente la materia");
        this.router.navigate(['']);
        console.log(res);
      },
      error: (err) => {
        this.notificationService.error("Algo salió mal", true);
        console.log(err);
      },
    });
  }

  cleanForm() {
    this.form.reset();
  }
}
