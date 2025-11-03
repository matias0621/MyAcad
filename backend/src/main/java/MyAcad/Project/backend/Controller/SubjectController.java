package MyAcad.Project.backend.Controller;

import MyAcad.Project.backend.Model.Subjects.SubjectsDTO;
import MyAcad.Project.backend.Model.Subjects.SubjectsEntity;

import MyAcad.Project.backend.Service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    // Modificar materia
    @PutMapping("/{id}")
    public ResponseEntity<SubjectsEntity> modifySubject(
            @PathVariable Long id,
            @RequestBody SubjectsEntity updatedSubject
    ) {
        return subjectService.modifySubject(id, updatedSubject);
    }

    @PutMapping("/prerequisite/{id}")
    public ResponseEntity<?> addPrerequisite(@PathVariable Long idSubject, @RequestBody List<Long> IdPrerequisite) {
        subjectService.addPrerequisite(IdPrerequisite, idSubject);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/prerequisite/delete/{id}")
    public ResponseEntity<?> deletePrerequisite(@PathVariable Long idSubject, @RequestBody Long IdPrerequisite) {
        subjectService.deleteAPrerequisite(idSubject, IdPrerequisite);
        return ResponseEntity.ok().build();
    }

    // Eliminar materia
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        return subjectService.deleteSubject(id);
    }

}
