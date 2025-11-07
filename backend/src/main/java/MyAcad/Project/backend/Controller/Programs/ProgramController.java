package MyAcad.Project.backend.Controller.Programs;

import MyAcad.Project.backend.Model.Programs.Program;
import MyAcad.Project.backend.Service.Programs.ProgramService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}