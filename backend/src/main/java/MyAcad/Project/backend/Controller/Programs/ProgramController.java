package MyAcad.Project.backend.Controller.Programs;

import MyAcad.Project.backend.Model.Programs.Program;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Service.Programs.ProgramService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/programs")
@AllArgsConstructor
public class ProgramController {

    private final ProgramService programService;

    @GetMapping
    public List<Program> findPrograms() {
        return programService.findPrograms();
    }


    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Program>> findByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(programService.findByStudent(studentId));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Program>> findByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(programService.findByTeacher(teacherId));
    }

    @GetMapping("/program/{name}")
    public ResponseEntity<Program> findByName(@PathVariable String name) {
        return ResponseEntity.ok(programService.findByName(name));
    }

    @PutMapping("/register-student/{name}")
    public ResponseEntity<?> registerStudent(@PathVariable String name, @RequestBody String legajoStudent) {
        programService.registerStudent(name, legajoStudent);
        return ResponseEntity.ok().build();
    }

    

}