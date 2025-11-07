import { Subject } from 'rxjs';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProgramsForm } from '../programs-form/programs-form';
import { ProgramsEditForm } from '../programs-edit-form/programs-edit-form';
import { CareerService } from '../../../Services/CareerService/career-service';
import { NotificationService } from '../../../Services/notification/notification.service';
import { Router } from '@angular/router';
import { SubjectsService } from '../../../Services/Subjects/subjects-service';
import Subjects from '../../../Models/Subjects/Subjects';


@Component({
  selector: 'app-programs-list',
  imports: [FormsModule, ProgramsForm, ProgramsEditForm],
  templateUrl: './programs-list.html',
  styleUrl: './programs-list.css'
})
export class ProgramsList implements OnInit{
  @Input()
  endpoint = ""
  @Output()
  program = new EventEmitter<any>;

  programs !: any[]
  search: string = ''
  timeout: any;
  showDisabled = false;
  allPrograms!: any[];
  selectedProgram: any = null;
  careerName:string | null = null

  constructor(
    private service: CareerService,
    public subjectsService: SubjectsService,
    private router:Router,
    private notificationService: NotificationService

  ) { }

  ngOnInit(): void {
    this.getCareers();
    this.getSubjects();
  }

  getCareers() {
    this.service.getCareers(this.endpoint).subscribe({
      next: (data) => { 
        this.programs = data;
        this.allPrograms = data;
      },
      error: (error) => {
        console.error('Error al obtener programas:', error);
      }
    })
  }

  getSubjects(){
    this.subjectsService.getAllSubject().subscribe({
      next: (res) => {
        this.subjectsService.listSubject = [...res]
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  addSubjectsToCareer(subjects:Subjects){
    if (this.careerName == null){
      alert("Seleccione una materia")
      return
    }

    const name = this.careerName

    this.subjectsService.addSubjectToCareer(name, subjects).subscribe({
      next: (res) => {
        alert("Se añadio la materia correctamente")
      },
      error: (err) => {
        alert("Hubo un problema al eliminar la materia")
      }
    })
  }

  saveNameCareer(name:string){
    this.careerName = name
  }

  deleteNameCareer(){
    this.careerName = null
  }

  deleteProgram(id: number) {
    this.notificationService.confirm(
      '¿Estás seguro de que deseas eliminar este programa?',
      'Confirmar eliminación',
      'Eliminar',
      'Cancelar'
    ).then((confirmed) => {
      if (confirmed) {
        this.service.deleteCareer(id, this.endpoint).subscribe({
          next: (data) => { 
            this.notificationService.success('Programa eliminado exitosamente');
            this.getCareers();
          },
          error: (error) => { 
            this.notificationService.error('Error al eliminar el programa. Por favor, intenta nuevamente', true);
          }
        });
      }
    });
  }

  modifyProgram(program : any){
    this.program.emit(program);
  }

  viewDisabled(item: any) {
    this.notificationService.confirm(
      `¿Deseas activar "${item.name}"?`,
      'Confirmar activación',
      'Activar',
      'Cancelar'
    ).then((confirmed) => {
      if (confirmed) {
        const updatedItem = { ...item, active: true };
        this.service.updateCareer(updatedItem, this.endpoint).subscribe({
          next: (response) => {
            this.notificationService.success(`${item.name} activado/a exitosamente`);
            this.getCareers();
          },
          error: (error) => {
            this.notificationService.error('Error al activar. Por favor, intenta nuevamente', true);
          }
        });
      }
    });
  }

  toggleDisabledView() {
    this.showDisabled = !this.showDisabled;
  }
  
  filter: string = '';
  filterPrograms() {
    if (this.filter === '') {
      this.programs = this.allPrograms;
    } else if (this.filter === 'Activos') {
      this.programs = this.allPrograms.filter(p => p.active === true);
    } else if (this.filter === 'Inactivos') {
      this.programs = this.allPrograms.filter(p => p.active === false);
    }
  }

  registerToStudent(nameProgram:string){
    this.service.setCareerSelected(nameProgram)
    this.router.navigate(['register-student-to-commission'])
  }
  
}