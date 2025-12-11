import { Component, OnDestroy, OnInit } from '@angular/core';
import { CareerService } from '../../Services/CareerService/career-service';
import { CommissionService } from '../../Services/Commission/commission-service';
import Career from '../../Models/Users/Careers/Career';
import Technical from '../../Models/Users/Careers/Technical';
import Course from '../../Models/Users/Careers/Course';
import Commission from '../../Models/Commission/commission';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import Subjects from '../../Models/Subjects/Subjects';
import { RegistrationStudentOrTeacher } from '../../Models/Users/Student';
import { NotificationService } from '../../Services/notification/notification.service';
import { AuthService } from '../../Services/Auth/auth-service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-inscription-to-commission',
  imports: [ReactiveFormsModule],
  templateUrl: './inscription-to-commission.html',
  styleUrl: './inscription-to-commission.css',
})
export class InscriptionToCommission implements OnInit {
  listCommission: Commission[] = [];
  form!: FormGroup;
  legajo!: FormControl;
  com!: Commission;
  sub!: Subjects;
  programSelected: string | null = null;
  programName!: string;
  selectedFile!: File
  commissionId!: number

  constructor(
    public careerService: CareerService,
    private notificationService: NotificationService,
    public auth: AuthService,
    public actRouter: ActivatedRoute,
    public commisionService: CommissionService
  ) {
    this.programName = this.actRouter.snapshot.params['name'];
    this.legajo = new FormControl('', Validators.required);
    this.form = new FormGroup({
      legajo: this.legajo,
    });
  }

  ngOnInit(): void {
    this.getCommisionByNameProgram();
    console.log(this.auth.getRole());
  }

  getCommisionByNameProgram() {
    this.commisionService.getByProgram(this.programName).subscribe({
      next: (res) => {
        this.listCommission = res;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  addStudentToCommision() {
    const request: RegistrationStudentOrTeacher = {
      legajo: this.legajo.value,
      subjectsId: this.sub.id,
    };
    if (this.form.invalid) {
      this.notificationService.warning(
        'Formulario inválido. Por favor, complete todos los campos correctamente.'
      );
      this.form.markAllAsTouched();
      return;
    }
    if (this.form.invalid) {
      this.notificationService.warning(
        'Formulario inválido. Por favor, complete todos los campos correctamente.'
      );
      this.form.markAllAsTouched();
      return;
    }

    this.commisionService.registerStudentToCommissionByManager(this.com.id, request).subscribe({
      next: (res) => {
        this.notificationService.success('Se registro el alumno correctamente');
      },
      error: (err) => {
        this.notificationService.error(err.error, true);
        console.log(err);
      },
    });
  }
  addTeacherToCommision() {
    const request: RegistrationStudentOrTeacher = {
      legajo: this.legajo.value,
      subjectsId: this.sub.id,
    };

    if (this.form.invalid) {
      this.notificationService.warning(
        'Formulario inválido. Por favor, complete todos los campos correctamente.'
      );
      this.form.markAllAsTouched();
      return;
    }

    if (this.form.invalid) {
      this.notificationService.warning(
        'Formulario inválido. Por favor, complete todos los campos correctamente.'
      );
      this.form.markAllAsTouched();
      return;
    }

    this.commisionService.registerTeacherToCommissionByManager(this.com.id, request).subscribe({
      next: (res) => {
        this.notificationService.success('Se registro el profesor correctamente');
      },
      error: (err) => {
        this.notificationService.error(err.error, true);
        console.log(err);
      },
    });
  }

  registerStudent(commissionId: number, subjectsId: number) {
    this.commisionService.regiterByStudent(commissionId, subjectsId).subscribe({
      next: (res) => {
        this.notificationService.success('Te registraste correctamente');
      },
      error: (err) => {
        this.notificationService.error('Hubo un error', true);
        console.log(err);
      },
    });
  }

  setCommissionAndSubject(com: Commission, s: Subjects) {
    this.com = com;
    this.sub = s;
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];

    if (file) {
      this.selectedFile = file;
      this.form.patchValue({ csv: file });
    }
  }

  setCommissionId(id:number){
    this.commissionId = id
  }

  uploadCsv() {
    if (!this.selectedFile){
      this.notificationService.warning('Debe subir un archivo CSV');
      return
    }

    const formData = new FormData()
    formData.append('file', this.selectedFile)

    this.commisionService.uploadCsv(this.commissionId, formData).subscribe({
      next: () => {
        this.notificationService.success('CSV procesado correctamente')
      },
      error: (err) => {
        this.notificationService.error("Error procesando el CSV")
        console.log(err)
      }
    })
  }

}
