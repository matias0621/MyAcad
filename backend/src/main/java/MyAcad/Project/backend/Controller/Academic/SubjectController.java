package MyAcad.Project.backend.Controller.Academic;

import MyAcad.Project.backend.Model.Academic.CommissionResponse;
import MyAcad.Project.backend.Model.Academic.SubjectsDTO;
import MyAcad.Project.backend.Model.Academic.SubjectsEntity;

import MyAcad.Project.backend.Model.Academic.SubjectsResponse;
import MyAcad.Project.backend.Service.Academic.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
@RequiredArgsConstructor

public class SubjectController {

    private final SubjectService subjectService;

    // Crear una nueva materia
    @PostMapping
    public ResponseEntity<SubjectsDTO> createSubject(@RequestBody SubjectsDTO subject) {
        subjectService.createSubject(subject);
        return ResponseEntity.ok(subject);
    }

    // Listar materias con paginación
    @GetMapping("/pagined")
    public Page<SubjectsEntity> listSubjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return subjectService.listSubject(page, size);
    }

    @GetMapping
    public ResponseEntity<List<SubjectsResponse>> getAllSubject(){
        return  ResponseEntity.ok(subjectService.getAllSubjects());
    }

    // Buscar materias por nombre (ignore case)
    @GetMapping("/search")
    public List<SubjectsResponse> searchByName(@RequestParam String name) {
        return subjectService.getByNameIgnoringCase(name);
    }

    // Obtener materia por ID
    @GetMapping("/{id}")
    public ResponseEntity<SubjectsEntity> getById(@PathVariable Long id) {
        return subjectService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/semester-less-than/{semester}")
    public ResponseEntity<List<SubjectsResponse>> getSubjectsBySemester(@PathVariable Integer semester) {
        return ResponseEntity.ok(subjectService.findBySemestersLessThan(semester));
    }

    @GetMapping("/semester-less-than-and-program/{program}/{semester}")
    public ResponseEntity<List<SubjectsResponse>> getSubjectsBySemesterLessThanAndProgram(@PathVariable String program, @PathVariable Integer semester) {
        return ResponseEntity.ok(subjectService.findByProgramAndSemesterLessThan(program,semester));
    }

    @GetMapping("/program/{program}")
    public ResponseEntity<List<SubjectsResponse>> getByProgram(@PathVariable String program) {
        return ResponseEntity.ok(subjectService.findByProgram(program));
    }

    // Modificar materia
    @PutMapping("/{id}")
    public ResponseEntity<?> modifySubject(
            @PathVariable Long id,
            @RequestBody SubjectsEntity updatedSubject
    ) {
        subjectService.modifySubject(id, updatedSubject);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/prerequisite-list/{id}")
    public ResponseEntity<?> addPrerequisiteList(@PathVariable Long id, @RequestBody List<Long> IdPrerequisite) {
        subjectService.addPrerequisiteList(IdPrerequisite, id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/prerequisite/{id}")
    public ResponseEntity<?> addPrerequisite(@PathVariable Long id, @RequestBody Long subjectPrerequiste){
        subjectService.addPrerequisite(subjectPrerequiste, id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/prerequisite/delete/{id}")
    public ResponseEntity<?> deletePrerequisite(@PathVariable Long id, @RequestBody Long IdPrerequisite) {
        subjectService.deleteAPrerequisite(IdPrerequisite, id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/add-subject-to-career/{nameCareer}")
    public ResponseEntity<?> addSubjectsToCareer(@PathVariable String nameCareer, @RequestBody SubjectsEntity subjects) {
        subjectService.addSubjectsToCareer(nameCareer, subjects);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/delete-subject-to-career/{nameCareer}")
    public ResponseEntity<?> deleteSubjectsToCareer(@PathVariable String nameCareer, @RequestBody SubjectsEntity subjects) {
        subjectService.deleteSubjectsToCareer(nameCareer, subjects);
        return ResponseEntity.ok().build();
    }

    // Baja lógica materia
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        return subjectService.deleteSubject(id);
    }

    // Baja definitiva materia
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> definitiveDeleteSubject(@PathVariable Long id) {
        return subjectService.definitiveDeleteSubject(id);
    }
}
