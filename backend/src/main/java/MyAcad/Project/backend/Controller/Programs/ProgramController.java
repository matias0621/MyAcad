package MyAcad.Project.backend.Controller.Programs;

import MyAcad.Project.backend.Enum.ProgramType;
import MyAcad.Project.backend.Model.Programs.*;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Service.Programs.ProgramService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
    public List<ProgramResponse> findPrograms() {
        return programService.findPrograms();
    }

    @GetMapping("/paginated")
    public Page<ProgramResponse> listProgramPaginated(@RequestParam(name = "page") int page,
                                            @RequestParam(name = "size") int size) {
        return programService.listProgramPaginated(page, size);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ProgramResponse>> findByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(programService.findByStudent(studentId));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<ProgramResponse>> findByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(programService.findByTeacher(teacherId));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ProgramResponse> findByName(@PathVariable String name) {
        return ResponseEntity.ok(programService.findByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(programService.findProgramById(id));
    }

    @GetMapping("/engineering")
    public ResponseEntity<List<ProgramResponse>> findEngineering() {
        return ResponseEntity.ok(programService.findByProgramType(ProgramType.ENGINEERING));
    }

    @GetMapping("/techinical")
    public ResponseEntity<List<ProgramResponse>> findTechinical() {
        return ResponseEntity.ok(programService.findByProgramType(ProgramType.TECHNICAL));
    }

    @GetMapping("/course")
    public ResponseEntity<List<ProgramResponse>> findCourse() {
        return ResponseEntity.ok(programService.findByProgramType(ProgramType.COURSE));
    }

    @PostMapping("/create-course")
    public ResponseEntity<?> createCourse(@RequestBody ProgramsDTO program) {
        programService.createCourse(program);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create-techinical")
    public ResponseEntity<?> createTechnical(@RequestBody ProgramsDTO program) {
        programService.createTechnical(program);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create-engineering")
    public ResponseEntity<?> createEngineering(@RequestBody ProgramsDTO program) {
        programService.createEngineering(program);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/register-student/{name}")
    public ResponseEntity<?> registerStudent(@PathVariable String name, @RequestBody String legajoStudent) {
        programService.registerStudent(name, legajoStudent);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/register-teacher/{name}")
    public ResponseEntity<?> registerTeacher(@PathVariable String name, @RequestBody String legajoStudent) {
        programService.registerTeacher(name, legajoStudent);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProgram(@PathVariable Long id, @RequestBody ProgramsDTO program) {
        programService.updateProgram(id, program);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/definitive-delete/{id}")
    public ResponseEntity<?> deleteProgram(@PathVariable Long id) {
        programService.deleteProgram(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/logic-delete/{id}")
    public ResponseEntity<?> deleteLogicProgram(@PathVariable Long id) {
        programService.logicDeleteProgram(id);
        return ResponseEntity.ok().build();
    }


}
