import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CareerService } from '../../Services/CareerService/career-service';

@Component({
  selector: 'app-programs-list',
  imports: [FormsModule],
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

  constructor(
    private service: CareerService
  ) { }

  ngOnInit(): void {
    this.getCareers();
  }

  getCareers() {
    this.service.getCareers(this.endpoint).subscribe({
      next: (data) => { this.programs = data },
      error: (error) => { console.error(error) }
    })
  }

  deleteProgram(id: number) {
    this.service.deleteCareer(id, this.endpoint).subscribe({
      next: (data) => { this.getCareers() },
      error: (error) => { console.error(error) }
    })
  }

  modifyProgram(program : any){
    this.program.emit(program);
  }

  viewDisabled(item: any) {
    if (confirm(`¿Deseas activar "${item.name}"?`)) {
      const updatedItem = { ...item, active: true };
      this.service.updateByEndpoint(updatedItem, this.endpoint).subscribe({
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
  
}
