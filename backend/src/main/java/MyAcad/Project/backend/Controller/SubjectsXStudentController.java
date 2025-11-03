package MyAcad.Project.backend.Controller;

import MyAcad.Project.backend.Model.Academic.SubjectsXStudentDTO;
import MyAcad.Project.backend.Model.Academic.SubjectsXStudentEntity;
import MyAcad.Project.backend.Service.SubjectsXStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject-x-student")
@RequiredArgsConstructor
public class SubjectsXStudentController {
    private final SubjectsXStudentService subjectsXStudentService;

    @GetMapping
    public ResponseEntity<List<SubjectsXStudentEntity>> getAllSubjectsXStudents() {
        return ResponseEntity.ok(subjectsXStudentService.getAllSubjectsXStudent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectsXStudentEntity> getSubjectsXStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(subjectsXStudentService.getSubjectsXStudentById(id));
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<List<SubjectsXStudentEntity>> getSubjectsXStudentByStudentId(@PathVariable Long id) {
        return ResponseEntity.ok(subjectsXStudentService.getAllSubjectsXStudentByStudentId(id));
    }

    @GetMapping("/student/{studentId}/subject/{subjectId}")
    public ResponseEntity<SubjectsXStudentEntity> getSubjectsXStudentBySubjectIdAndStudentId(@PathVariable Long studentId, @PathVariable Long subjectId) {
        return ResponseEntity.ok(subjectsXStudentService.getSubjectsXStudentByStudentIdAndSubjectsId(studentId, subjectId).orElseThrow());
    }

    @PostMapping()
    public ResponseEntity<SubjectsXStudentDTO> createSubjectsXStudent(@RequestBody SubjectsXStudentDTO subjectsXStudentDTO){
        subjectsXStudentService.createSubjectsXStudent(subjectsXStudentDTO);
        return ResponseEntity.ok().body(subjectsXStudentDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectsXStudentDTO> updateSubjectsXStudent(@PathVariable Long id, @RequestBody SubjectsXStudentDTO subjectsXStudentDTO){
        subjectsXStudentService.updateSubjectsXStudent(subjectsXStudentDTO, id);
        return ResponseEntity.ok().body(subjectsXStudentDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubjectsXStudent(@PathVariable Long id){
        subjectsXStudentService.deleteSubjectsXStudent(id);
        return ResponseEntity.ok().build();
    }

}

