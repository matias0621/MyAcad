
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProgramsForm } from '../programs-form/programs-form';
import { ProgramsEditForm } from '../programs-edit-form/programs-edit-form';
import { CareerService } from '../../../Services/CareerService/career-service';
import { NotificationService } from '../../../Services/notification/notification.service';
import { Router } from '@angular/router';
import { SubjectsService } from '../../../Services/Subjects/subjects-service';
import Subjects from '../../../Models/Subjects/Subjects';
import { DecimalPipe } from '@angular/common';


@Component({
  selector: 'app-programs-list',
  imports: [FormsModule, ProgramsForm, ProgramsEditForm, DecimalPipe],
  templateUrl: './programs-list.html',
  styleUrl: './programs-list.css'
})
export class ProgramsList implements OnInit {
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
  careerName: string | null = null
  // Paginación
  totalPages: number = 0;
  currentPage: number = 0;

  constructor(
    private service: CareerService,
    public subjectsService: SubjectsService,
    private router: Router,
    private notificationService: NotificationService

  ) { }

  ngOnInit(): void {
    this.getCareers();
    this.getSubjects();
  }

  getCareers(page: number = 0, size: number = 10) {
    this.service.getCareersPaginated(this.endpoint, page, size).subscribe({
      next: (data) => {
        this.programs = data.content;
        this.allPrograms = data.content;
        this.totalPages = data.totalPages;
        this.currentPage = data.number;
      },
      error: (error) => {
        console.error('Error al obtener programas:', error);
      }
    })
  }

  getSubjects() {
    this.subjectsService.getAllSubject().subscribe({
      next: (res) => {
        this.subjectsService.listSubject = [...res]
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  addSubjectsToCareer(subjects: Subjects) {
    if (this.careerName == null) {
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

  saveNameCareer(name: string) {
    this.careerName = name
  }

  deleteNameCareer() {
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

  definitiveDeleteProgram(id: number) {
    this.notificationService.confirm(
      '¿Estás seguro de que deseas eliminar permanentemente este programa?',
      'Confirmar eliminación permanente',
      'Eliminar',
      'Cancelar'
    ).then((confirmed) => {
      if (confirmed) {
        this.service.definitiveDeleteCareer(id, this.endpoint).subscribe({
          next: (data) => {
            this.notificationService.success('Programa eliminado exitosamente');
            this.getCareers();
          },
          error: (error) => {
            const errorMessage = this.getErrorMessage(error, 'programa');
            this.notificationService.error(errorMessage, true);
          }
        });
      }
    });
  }

  modifyProgram(program: any) {
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

  registerToStudent() {
    this.router.navigate(['/show-career-for-register']);
  }

  private getErrorMessage(error: any, entityType: string): string {
    let errorMessage = `Error al eliminar el ${entityType}. Por favor, intenta nuevamente`;
    
    if (error?.error) {
      if (typeof error.error === 'string') {
        errorMessage = error.error;
      } else if (error.error?.message) {
        errorMessage = error.error.message;
      } else if (error.error?.error) {
        errorMessage = error.error.error;
      }
    } else if (error?.message) {
      errorMessage = error.message;
    }
    
    if (errorMessage.includes('Unable to find')) {
      if (errorMessage.includes('Program')) {
        errorMessage = 'No se puede eliminar porque hay un problema con los programas asociados. Verifica que todas las relaciones estén correctas.';
      } else if (errorMessage.includes('Student')) {
        errorMessage = 'No se puede eliminar porque hay un problema con los estudiantes asociados.';
      } else if (errorMessage.includes('Teacher')) {
        errorMessage = 'No se puede eliminar porque hay un problema con los profesores asociados.';
      } else {
        errorMessage = 'No se puede eliminar porque hay relaciones asociadas que no se pueden procesar.';
      }
    }
    
    if (errorMessage.includes('with id 0')) {
      errorMessage = 'No se puede eliminar porque hay datos incompletos o inválidos en las relaciones asociadas.';
    }
    
    if (errorMessage.includes('foreign key constraint') || errorMessage.includes('constraint')) {
      errorMessage = 'No se puede eliminar porque está asociado a otros registros en el sistema.';
    }
    
    if (errorMessage.includes('Transaction silently rolled back') || errorMessage.includes('rollback-only')) {
      errorMessage = 'No se puede eliminar porque hay relaciones asociadas que impiden la eliminación. Verifica los estudiantes, profesores o materias asignados.';
    }
    
    return errorMessage;
  }

}