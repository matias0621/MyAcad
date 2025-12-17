import { Component, EventEmitter, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InscriptionToCommissionService } from '../../Services/InscriptionToCommission/inscription-to-commission-service';
import { ProgramService } from '../../Services/Program/program-service';
import { NotificationService } from '../../Services/notification/notification.service';
import Program from '../../Models/Program/Program';
import { InscriptionToCommission, PostInscriptionToCommission } from '../../Models/InscriptionToCommission/InscritionToCommission';
import { CommissionService } from '../../Services/Commission/commission-service';
import Commission from '../../Models/Commission/commission';

@Component({
  selector: 'app-inscription-commission-for-student-form',
  imports: [ReactiveFormsModule],
  templateUrl: './inscription-commission-for-student-form.html',
  styleUrl: './inscription-commission-for-student-form.css'
})
export class InscriptionCommissionForStudentForm {
   @Output() inscriptionCreated = new EventEmitter<void>();
  form!: FormGroup;
  inscriptionDate!: FormControl;
  finishDate!: FormControl;
  programName!:FormControl
  programs!:Program[]
  inscriptionList!:InscriptionToCommission[]
  commissionList!:Commission[]
  commissionId: number | null | undefined

  constructor(
    public inscriptionsCommission:InscriptionToCommissionService,
    public commissionService:CommissionService,
    public programService:ProgramService,
    private notificationService : NotificationService
  ) {
    this.inscriptionDate = new FormControl('', [Validators.required]);
    this.finishDate = new FormControl('', [Validators.required]);
    this.programName = new FormControl('', [Validators.required])

    this.form = new FormGroup({
      inscriptionDate: this.inscriptionDate,
      finishDate: this.finishDate,
      programName:this.programName
    });
  }

  ngOnInit(): void {
    this.getInscription();
    this.getProgram();

    this.programName.valueChanges.subscribe({
      next: () => {
        this.commissionId = undefined;
        this.getCommissionbyProgramName(this.programName.value)
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  addToCommission(id: number) {
    this.commissionId = id;
    this.notificationService.info('Comision asignada a la inscripción.');
  }

  getInscription() {
    this.inscriptionsCommission.getAllInscriptions().subscribe({
      next: (res:any) => {
        this.inscriptionList = res
      },
      error: (err:any) => {
        console.log(err);
      },
    });
  }


  getProgram() {
    this.programService.getPrograms().subscribe({
      next: (res) => {
        this.programs = res
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  getCommissionbyProgramName(programName:string){
    this.commissionService.getByProgram(programName).subscribe({
      next: (res) => {
        this.commissionList = res
      },
      error:(err) => {
        console.log(err)
      },
    })
  }

  OnSubmit() {
    if (this.commissionId === null || this.commissionId === undefined) {
      this.notificationService.error('Debe seleccionar una materia antes de inscribirse al examen.', true);
      return;
    }

    if (this.form.invalid) {
      this.notificationService.warning('Formulario inválido. Por favor, complete todos los campos correctamente.');
      this.form.markAllAsTouched();
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

    const inscription:PostInscriptionToCommission = {
      commissionId: this.commissionId,
      inscriptionDate: formatDate(this.inscriptionDate.value),
      finishInscriptionDate: formatDate(this.finishDate.value)
    }

    this.inscriptionsCommission.saveInscription(inscription).subscribe({
      next: () => {
        this.notificationService.success("Se creo con exito la nueva inscripcion")
        this.form.reset();
        this.commissionId = undefined;
        this.inscriptionCreated.emit();
      },
      error: (err) => {
        console.log(err)
        this.notificationService.error("Hubo un error", true)
      }
    })

  }

}
