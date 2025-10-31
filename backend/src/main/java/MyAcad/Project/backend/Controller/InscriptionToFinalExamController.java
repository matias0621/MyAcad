package MyAcad.Project.backend.Controller;

import MyAcad.Project.backend.Model.InscriptionToFinalExam.InscriptionToFinalExamDTO;
import MyAcad.Project.backend.Model.InscriptionToFinalExam.InscriptionToFinalExamEntity;
import MyAcad.Project.backend.Service.InscriptionToFinalExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inscription-final-exam")
public class InscriptionToFinalExamController {

    private InscriptionToFinalExamService inscriptionToFinalExamService;

    @GetMapping
    public ResponseEntity<List<InscriptionToFinalExamEntity>> findAll() {
        return ResponseEntity.ok(inscriptionToFinalExamService.getAllInscriptions());
    }

    @GetMapping("/date-inscription/{date}")
    public ResponseEntity<List<InscriptionToFinalExamEntity>> findByInscriptionDate(@PathVariable String date) {
        return ResponseEntity.ok(inscriptionToFinalExamService.getAllInscriptionsByInscriptionDate(date));
    }

    @GetMapping("/date-exam/{date}")
    public ResponseEntity<List<InscriptionToFinalExamEntity>> findByInscriptionDateExam(@PathVariable String date) {
        return ResponseEntity.ok(inscriptionToFinalExamService.getAllInscriptionsByExamDate(date));
    }

    @GetMapping("subjects/{id}")
    public ResponseEntity<List<InscriptionToFinalExamEntity>> findBySubjectId(@PathVariable Long id) {
        return ResponseEntity.ok(inscriptionToFinalExamService.getAllInscriptionsBySubjectId(id));
    }

    @PostMapping
    public ResponseEntity<InscriptionToFinalExamDTO> save(@RequestBody InscriptionToFinalExamDTO entity) {
        inscriptionToFinalExamService.createInscription(entity);
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InscriptionToFinalExamDTO> update(@PathVariable Long id, @RequestBody InscriptionToFinalExamDTO entity) {
        inscriptionToFinalExamService.updateInscription(entity, id);
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/register-student-for-exam/{id}")
    public ResponseEntity<InscriptionToFinalExamEntity> inscriptionAtStudentToExam(@PathVariable Long id, @RequestBody Long IdStudent) {
        return ResponseEntity.ok(inscriptionToFinalExamService.addToStudent(id, IdStudent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inscriptionToFinalExamService.deleteInscription(id);
        return ResponseEntity.noContent().build();
    }
}
