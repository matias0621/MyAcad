package MyAcad.Project.backend.Controller.Academic;

import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Service.Academic.CertificateService;
import MyAcad.Project.backend.Service.Users.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certificate")
public class CertificateController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CertificateService certificateService;

    @GetMapping("/regular-student/{studentId}")
    public ResponseEntity<byte[]> downloadRegularStudentCertificate(@PathVariable Long studentId) throws Exception {

        Student student = studentService.getById(studentId).get();

        byte[] pdf = certificateService.generateRegularStudentCertificate(student);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificado.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/academic-activity/{studentId}")
    public ResponseEntity<byte[]> downloadAcademicActivityCertificate(@PathVariable Long studentId) throws Exception {

        Student student = studentService.getById(studentId).get();

        byte[] pdf = certificateService.generateAcademicActivityCertificate(student);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificado.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}

