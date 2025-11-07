package MyAcad.Project.backend.Controller.Academic;

import MyAcad.Project.backend.Model.Academic.SubjectsDTO;
import MyAcad.Project.backend.Model.Academic.SubjectsEntity;

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
    public ResponseEntity<List<SubjectsEntity>> getAllSubject(){
        return  ResponseEntity.ok(subjectService.getAllSubjects());
    }

    // Buscar materias por nombre (ignore case)
    @GetMapping("/search")
    public List<SubjectsEntity> searchByName(@RequestParam String name) {
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
    public ResponseEntity<List<SubjectsEntity>> getSubjectsBySemester(@PathVariable Integer semester) {
        return ResponseEntity.ok(subjectService.findBySemestersLessThan(semester));
    }

    // Modificar materia
    @PutMapping("/{id}")
    public ResponseEntity<SubjectsEntity> modifySubject(
            @PathVariable Long id,
            @RequestBody SubjectsEntity updatedSubject
    ) {
        return subjectService.modifySubject(id, updatedSubject);
    }

    @PutMapping(value = "/prerequisite/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addPrerequisite(@PathVariable Long id, @RequestBody List<Long> IdPrerequisite) {
        subjectService.addPrerequisite(IdPrerequisite, id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/prerequisite/delete/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePrerequisite(@PathVariable Long idSubject, @RequestBody Long IdPrerequisite) {
        subjectService.deleteAPrerequisite(idSubject, IdPrerequisite);
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

    // Eliminar materia
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        return subjectService.deleteSubject(id);
    }

}
