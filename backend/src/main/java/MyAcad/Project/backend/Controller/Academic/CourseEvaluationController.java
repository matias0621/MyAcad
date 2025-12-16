package MyAcad.Project.backend.Controller.Academic;

import MyAcad.Project.backend.Model.Academic.CourseEvaluationDTO;
import MyAcad.Project.backend.Model.Academic.CourseEvaluationResponse;
import MyAcad.Project.backend.Service.Academic.CourseEvaluationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/course-evaluations")
@AllArgsConstructor
public class CourseEvaluationController {
    private final CourseEvaluationService courseEvaluationService;

    // ===================== CREATE =====================

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CourseEvaluationDTO dto) {
        courseEvaluationService.createCourseEvaluation(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // ===================== GET ALL =====================

    @GetMapping
    public ResponseEntity<List<CourseEvaluationResponse>> getAll() {
        return ResponseEntity.ok(
                courseEvaluationService.getAllCourseEvaluations()
        );
    }

    // ===================== GET BY ID =====================

    @GetMapping("/{id}")
    public ResponseEntity<CourseEvaluationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                courseEvaluationService.getCourseEvaluationById(id)
        );
    }

    // ===================== GET BY SUBJECT =====================

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<CourseEvaluationResponse>> getBySubject(
            @PathVariable Long subjectId
    ) {
        return ResponseEntity.ok(
                courseEvaluationService.getAllCourseEvaluationsBySubjectId(subjectId)
        );
    }

    // ===================== GET BY TEACHER =====================

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<CourseEvaluationResponse>> getByTeacher(
            @PathVariable Long teacherId
    ) {
        return ResponseEntity.ok(
                courseEvaluationService.getAllCourseEvaluationsByTeacherId(teacherId)
        );
    }

    // ===================== PAGINATED =====================

    @GetMapping("/paginated")
    public ResponseEntity<Page<CourseEvaluationResponse>> getPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                courseEvaluationService.getAllCourseEvaluationsPaginated(page, size)
        );
    }

    @GetMapping("/paginated/subject/{subjectId}")
    public ResponseEntity<Page<CourseEvaluationResponse>> getPaginatedBySubject(
            @PathVariable Long subjectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                courseEvaluationService.getAllCourseEvaluationsPaginatedBySubjectId(subjectId, page, size)
        );
    }

    @GetMapping("/paginated/teacher/{teacherId}")
    public ResponseEntity<Page<CourseEvaluationResponse>> getPaginatedByTeacher(
            @PathVariable Long teacherId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                courseEvaluationService.getAllCourseEvaluationsPaginatedByTeacherId(teacherId, page, size)
        );
    }

    // ===================== GET BY DATE =====================

    @GetMapping("/paginated/date")
    public ResponseEntity<Page<CourseEvaluationResponse>> getPaginatedByDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                courseEvaluationService.getAllCourseEvaluationsPaginatedByDate(date, page, size)
        );
    }
}

