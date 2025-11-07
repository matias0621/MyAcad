import { Component } from '@angular/core';
import Commission from '../../Models/Commission/commission';
import Subjects from '../../Models/Subjects/Subjects';
import { ActivatedRoute, Router } from '@angular/router';
import { SubjectsService } from '../../Services/Subjects/subjects-service';
import { CommissionService } from '../../Services/Commission/commission-service';
import { SubjectForm } from '../../components/Academic/subject-form/subject-form';
import { SubjectFormEdit } from '../../components/Academic/subject-form-edit/subject-form-edit';

@Component({
  selector: 'app-subjects-page',
  imports: [SubjectForm, SubjectFormEdit],
  templateUrl: './subjects-page.html',
  styleUrl: './subjects-page.css'
})
export class SubjectsPage {

  id:string|undefined
  listCommission:Commission[] = []
  subjects!:Subjects

  constructor(
    public subjectService: SubjectsService,
    private router: Router,
    public commissionService:CommissionService,
    private activatedRoute: ActivatedRoute
  ) {
    this.id = this.activatedRoute.snapshot.params['id']
  }

  ngOnInit(): void {
    this.getAllSubject();
    this.getAllCommission();
  }

  getAllSubject() {
    this.subjectService.getAllSubject().subscribe({
      next: (res) => {
        console.log(res)
        this.subjectService.listSubject = res;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  getAllCommission(){
    this.commissionService.getCommissions().subscribe({
      next: (res) => {
        this.listCommission = res
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  deleteSubject(id: number) {
    this.subjectService.deleteSubject(id.toString()).subscribe({
      next: (res) => {
        alert('Se elimino correctamente');
        this.getAllSubject();
      },
      error: (err) => {
        alert('No se pudo eliminar la materia');
        console.log(err)
      },
    });
  }

  setSubjects(s:Subjects){
    this.subjects = s
  }

  redirecTo(id:number){
    this.router.navigate(["/subject/", id])
  }

  addToCommission(idCommission:number){
    this.commissionService.addSubjectsToCommission(idCommission, this.subjects.id).subscribe({
      next: (res) => {
        alert("Se añadio la materia a la comision y carrera")
      },
      error: (err) => {
        alert("Hubo un error")
        console.log(err)
      }
    })
  }
}
