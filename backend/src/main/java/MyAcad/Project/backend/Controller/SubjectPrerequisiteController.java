package MyAcad.Project.backend.Controller;

import MyAcad.Project.backend.Enum.AcademicStatus;
import MyAcad.Project.backend.Model.Academic.SubjectPrerequisiteDTO;
import MyAcad.Project.backend.Model.Academic.SubjectPrerequisiteEntity;
import MyAcad.Project.backend.Model.Academic.SubjectPrerequisiteResponse;
import MyAcad.Project.backend.Service.SubjectPrerequisiteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/SubjectPrerrequisite")
@AllArgsConstructor
public class SubjectPrerequisiteController {
    private final SubjectPrerequisiteService subjectPrerequisiteService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SubjectPrerequisiteDTO dto) {
        subjectPrerequisiteService.createSubjectPrerequisite(dto);
        return ResponseEntity.ok().build();
    }

    // ===================== UPDATE =====================

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody  SubjectPrerequisiteDTO dto) {
        subjectPrerequisiteService.updateSubjectPrerequisite(dto, id);
        return ResponseEntity.ok().build();
    }

    // ===================== DELETE =====================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subjectPrerequisiteService.deleteSubjectPrerequisite(id);
        return ResponseEntity.noContent().build();
    }

    // ===================== READ =====================

    @GetMapping
    public ResponseEntity<List<SubjectPrerequisiteResponse>> findAll() {
        return ResponseEntity.ok(
                subjectPrerequisiteService.findAllSubjectPrerequisites()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectPrerequisiteResponse> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                subjectPrerequisiteService.findById(id)
        );
    }

    // ===================== FILTERS =====================

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<SubjectPrerequisiteResponse>> findBySubject(
            @PathVariable Long subjectId
    ) {
        return ResponseEntity.ok(
                subjectPrerequisiteService.findAllSubjectPrerequisitesBySubjectId(subjectId)
        );
    }

    @GetMapping("/prerequisite/{prerequisiteId}")
    public ResponseEntity<List<SubjectPrerequisiteResponse>> findByPrerequisite(
            @PathVariable Long prerequisiteId
    ) {
        return ResponseEntity.ok(
                subjectPrerequisiteService.findAllSubjectPrerequisitesByPrerequisiteId(prerequisiteId)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SubjectPrerequisiteResponse>> findByRequiredStatus(
            @PathVariable AcademicStatus status
    ) {
        return ResponseEntity.ok(
                subjectPrerequisiteService.findAllSubjectPrerequisitesByRequiredStatus(status)
        );
    }

    // ===================== SPECIAL =====================

    @GetMapping("/exists")
    public ResponseEntity<Boolean> exists(
            @RequestParam Long subjectId,
            @RequestParam Long prerequisiteId
    ) {
        return ResponseEntity.ok(
                subjectPrerequisiteService.existsBySubjectIdAndPrerequisiteId(
                        subjectId,
                        prerequisiteId
                )
        );
    }

    @GetMapping("/subject-prerequisite")
    public ResponseEntity<SubjectPrerequisiteResponse> findBySubjectAndPrerequisite(
            @RequestParam Long subjectId,
            @RequestParam Long prerequisiteId
    ) {
        return ResponseEntity.ok(
                subjectPrerequisiteService.findBySubjectIdAndPrerequisiteId(
                        subjectId,
                        prerequisiteId
                )
        );
    }

    @GetMapping("/program/{program}")
    public ResponseEntity<List<SubjectPrerequisiteResponse>> findByProgram(
            @PathVariable String program
    ) {
        return ResponseEntity.ok(
                subjectPrerequisiteService.findByProgram(program)
        );
    }

    @GetMapping("/program/{program}/status/{status}")
    public ResponseEntity<List<SubjectPrerequisiteResponse>> findSubjectsRequiringStatus(
            @PathVariable String program,
            @PathVariable AcademicStatus status
    ) {
        return ResponseEntity.ok(
                subjectPrerequisiteService.findSubjectsRequiringStatus(program, status)
        );
    }

    @DeleteMapping("/{subjectId}/{prerequisiteId}")
    public ResponseEntity<Void> delete(@PathVariable Long subjectId, @PathVariable Long prerequisiteId) {
        subjectPrerequisiteService.deletePrerequisite(subjectId, prerequisiteId);
        return ResponseEntity.noContent().build();
    }

}
