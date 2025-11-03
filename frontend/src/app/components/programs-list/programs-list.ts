import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CareerService } from '../../Services/CareerService/career-service';
import { ProgramsForm } from '../programs-form/programs-form';
import { ProgramsEditForm } from '../programs-edit-form/programs-edit-form';

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

  constructor(
    private service: CareerService
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
    if (confirm('¿Estás seguro de que deseas eliminar este programa?')) {
      this.service.deleteCareer(id, this.endpoint).subscribe({
        next: (data) => { 
          alert('Programa eliminado exitosamente.');
          this.getCareers();
        },
        error: (error) => { 
          alert('Error al eliminar el programa. Por favor, intenta nuevamente.');
        }
      });
    }
  }

  modifyProgram(program : any){
    this.program.emit(program);
  }

  viewDisabled(item: any) {
    if (confirm(`¿Deseas activar "${item.name}"?`)) {
      const updatedItem = { ...item, active: true };
      this.service.updateCareer(updatedItem, this.endpoint).subscribe({
        next: (response) => {
          alert(`${item.name} activado/a exitosamente.`);
          this.getCareers();
        },
        error: (error) => {
          alert('Error al activar. Por favor, intenta nuevamente.');
        }
      });
    }
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
