import { Component } from '@angular/core';
import { CertificateService } from '../../Services/CertificateService/certificate-service';
import { AuthService } from '../../Services/Auth/auth-service';

@Component({
  selector: 'app-certificates',
  imports: [],
  templateUrl: './certificates.html',
  styleUrl: './certificates.css'
})
export class Certificates {
  studentId?: number;

  constructor(
    private certificateService: CertificateService,
    private authService: AuthService
  ) {
    const decoded: any = authService.getDecodedToken();
    this.studentId = decoded ? decoded.id : null;
  }


  downloadCertificate() {

    if (this.studentId) {
      this.certificateService.downloadCertificate(this.studentId).subscribe((pdf) => {
        const url = window.URL.createObjectURL(pdf);
        const a = document.createElement("a");
        a.href = url;
        a.download = "certificado_alumno_regular.pdf";
        a.click();
        window.URL.revokeObjectURL(url);
      });
    }else{
      console.error("Error al obtener el id del usuario.")
    }
  }
}
