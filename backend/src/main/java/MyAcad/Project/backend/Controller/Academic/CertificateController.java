package MyAcad.Project.backend.Controller.Academic;

import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Repository.Users.StudentRepository;
import MyAcad.Project.backend.Service.Academic.CertificateService;
import MyAcad.Project.backend.Service.Users.StudentService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CertificateController {


    private final StudentService studentService;

    private final StudentRepository studentRepository;

    private final CertificateService certificateService;

    @GetMapping("/regular-student/{studentId}")
    public ResponseEntity<byte[]> downloadRegularStudentCertificate(@PathVariable Long studentId) throws Exception {

        if (studentRepository.findById(studentId).isPresent()){
            Student student = studentRepository.findById(studentId).get();
            byte[] pdf = certificateService.generateRegularStudentCertificate(student);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ student.getLegajo()+"_alumno_regular_UTN.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        }else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/academic-activity/{studentId}")
    public ResponseEntity<byte[]> downloadAcademicActivityCertificate(@PathVariable Long studentId) throws Exception {

        if (studentRepository.findById(studentId).isPresent()){
            Student student = studentRepository.findById(studentId).get();

            byte[] pdf = certificateService.generateAcademicActivityCertificate(student);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ student.getLegajo() + "_actividad_académica_UTN.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

}

