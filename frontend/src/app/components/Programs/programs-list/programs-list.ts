import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProgramsForm } from '../programs-form/programs-form';
import { ProgramsEditForm } from '../programs-edit-form/programs-edit-form';
import { CareerService } from '../../../Services/CareerService/career-service';
import { NotificationService } from '../../../Services/notification/notification.service';

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

  constructor(
    private service: CareerService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.getCareers();
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
  
}