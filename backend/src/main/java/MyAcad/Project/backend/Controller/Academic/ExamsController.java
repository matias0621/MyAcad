package MyAcad.Project.backend.Controller.Academic;

import MyAcad.Project.backend.Exception.ExamException;
import MyAcad.Project.backend.Model.Academic.ExamsDTO;
import MyAcad.Project.backend.Model.Academic.ExamsResponse;
import MyAcad.Project.backend.Service.Academic.ExamsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
public class ExamsController {
    private final ExamsService examsService;

    @PostMapping
    public ResponseEntity<?> createFinalExam(@Valid @RequestBody ExamsDTO dto) {
        examsService.create(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ExamsResponse>> getAllFinalExams() {
        List<ExamsResponse> exams = examsService.findAll();
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<ExamsResponse>> getAllFinalExamsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ExamsResponse> exams = examsService.listExamsPaginated(page, size);
        return ResponseEntity.ok(exams);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ExamsResponse> getFinalExamById(@PathVariable Long id) {
        ExamsResponse exam = examsService.findById(id);
        return ResponseEntity.ok(exam);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<List<ExamsResponse>> getStudentFinalExamById(@PathVariable Long id) {
        return ResponseEntity.ok(examsService.findByStudentId(id));
    }

    @GetMapping("/student/{studentId}/program/{program}")
    public ResponseEntity<List<ExamsResponse>> getStudentFinalExamById(@PathVariable Long studentId, @PathVariable String program) {
        System.out.println("AAAAA" + program + "asd" + studentId);
        return ResponseEntity.ok(examsService.findByStudentIdAndProgram(studentId, program));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFinalExam(@PathVariable Long id,
                                                       @Valid @RequestBody ExamsDTO dto) {
        examsService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    //Baja lógica
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinalExam(@PathVariable Long id) {
        examsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Baja definitiva
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> definitiveDeleteFinalExam(@PathVariable Long id) {
        return examsService.definitiveDeleteFinalExam(id);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ExamsResponse>> findByScore(@RequestParam int score) {
        List<ExamsResponse> exams = examsService.findByScore(score);
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/subjects/{id}")
    public ResponseEntity<List<ExamsResponse>> findBySubjectId(@PathVariable Long id) {
        return ResponseEntity.ok(examsService.findAllBySubjectsId(id));
    }
}
