package MyAcad.Project.backend.Controller.Academic;

import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.StudentResponse;
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

    @GetMapping("/{studentId}")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable Long studentId) throws Exception {

        StudentResponse student = studentService.getById(studentId).get();
        Student stud = new Student();
        stud.setId(studentId);
        stud.setName(student.getName());
        stud.setEmail(student.getEmail());
        stud.setDni(student.getDni());
        stud.setRole(student.getRole());
        stud.setLastName(student.getLastName());
        stud.setActive(student.isActive());
        stud.setPassword(stud.getPassword());
        stud.setLegajo(student.getLegajo());


        byte[] pdf = certificateService.generateCertificate(stud);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificado.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}

