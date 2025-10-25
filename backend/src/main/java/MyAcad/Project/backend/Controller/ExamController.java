package MyAcad.Project.backend.Controller;

import MyAcad.Project.backend.Model.Exam.ExamDTO;
import MyAcad.Project.backend.Model.Exam.ExamEntity;
import MyAcad.Project.backend.Service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @GetMapping
    public ResponseEntity<List<ExamEntity>> getAllExams() {
        return ResponseEntity.ok(examService.getAllExams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamEntity> getExamById(@PathVariable int id) {
        return ResponseEntity.ok(examService.getExamById((long)id));
    }

    @GetMapping("/subjects/{id}")
    public ResponseEntity<List<ExamEntity>> getExamAllBySubject(@PathVariable int id) {
        return ResponseEntity.ok(examService.getAllExamsBySubjectsId((long) id));
    }

    @PostMapping
    public ResponseEntity<ExamDTO> addExam(@RequestBody ExamDTO exam) {
        examService.createExam(exam);
        return ResponseEntity.ok(exam);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamDTO> updateExam(@PathVariable int id, @RequestBody ExamDTO exam) {
        examService.updateExam(exam, (long)id);
        return ResponseEntity.ok(exam);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable int id) {
        return examService.deleteExam((long)id);
    }

}
