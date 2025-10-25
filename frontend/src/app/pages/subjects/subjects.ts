import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { SubjectForm } from '../../components/subject-form/subject-form';
import { SubjectsService } from '../../Services/Subjects/subjects-service';
import { Router, ActivatedRoute } from '@angular/router';
import { SubjectFormEdit } from '../../components/subject-form-edit/subject-form-edit';

@Component({
  selector: 'app-subjects',
  imports: [ReactiveFormsModule, SubjectForm, SubjectFormEdit],
  templateUrl: './subjects.html',
  styleUrl: './subjects.css',
})
export class Subjects implements OnInit {

  id:string|undefined

  constructor(
    public subjectService: SubjectsService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {
    this.id = this.activatedRoute.snapshot.params['id']
  }

  ngOnInit(): void {
    this.getAllSubject();
  }

  getAllSubject() {
    this.subjectService.getAllSubject().subscribe({
      next: (res) => {
        this.subjectService.listSubject = res;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  deleteSubject(id: number) {
    this.subjectService.deleteSubject(id.toString()).subscribe({
      next: (res) => {
        alert('Se elimino correctamente');
        this.getAllSubject();
      },
      error: (err) => {
        alert('No se pudo eliminar la materia');
      },
    });
  }

  redirecTo(id:number){
    this.router.navigate(["/subject/", id])
  }
}
