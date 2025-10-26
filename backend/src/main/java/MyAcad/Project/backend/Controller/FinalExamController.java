package MyAcad.Project.backend.Controller;

import MyAcad.Project.backend.Model.FinalExam.FinalExamDTO;
import MyAcad.Project.backend.Model.FinalExam.FinalExamEntity;
import MyAcad.Project.backend.Service.FinalExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/final-exam")
@RequiredArgsConstructor
public class FinalExamController {
    private final FinalExamService finalExamService;

    @PostMapping
    public ResponseEntity<FinalExamEntity> createFinalExam(@Valid @RequestBody FinalExamDTO dto) {
        FinalExamEntity createdExam = finalExamService.create(dto);
        return ResponseEntity.ok(createdExam);
    }

    @GetMapping
    public ResponseEntity<List<FinalExamEntity>> getAllFinalExams() {
        List<FinalExamEntity> exams = finalExamService.findAll();
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinalExamEntity> getFinalExamById(@PathVariable Long id) {
        FinalExamEntity exam = finalExamService.findById(id);
        return ResponseEntity.ok(exam);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinalExamEntity> updateFinalExam(@PathVariable Long id,
                                                           @Valid @RequestBody FinalExamDTO dto) {
        FinalExamEntity updatedExam = finalExamService.update(id, dto);
        return ResponseEntity.ok(updatedExam);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinalExam(@PathVariable Long id) {
        finalExamService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<FinalExamEntity>> findByScore(@RequestParam int score) {
        List<FinalExamEntity> exams = finalExamService.findByScore(score);
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/subjects/{id}")
    public ResponseEntity<List<FinalExamEntity>> findBySubjectId(@PathVariable Long id) {
        return ResponseEntity.ok(finalExamService.findAllBySubjectsId(id));
    }
}
