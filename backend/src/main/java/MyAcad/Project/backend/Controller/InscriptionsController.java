package MyAcad.Project.backend.Controller;

import MyAcad.Project.backend.Configuration.UserDetailsImpl;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission.InscriptionToCommissionDTO;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission.InscriptionToCommissionResponse;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToFinalExam.InscriptionToFinalExamDTO;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToFinalExam.InscriptionToFinalExamEntity;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToFinalExam.InscriptionToFinalExamResponse;
import MyAcad.Project.backend.Service.Inscriptions.InscriptionToCommissionService;
import MyAcad.Project.backend.Service.Inscriptions.InscriptionToFinalExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inscriptions")
@RequiredArgsConstructor
public class InscriptionsController {

    private final InscriptionToFinalExamService inscriptionToFinalExamService;
    private final InscriptionToCommissionService inscriptionToCommissionService;

    @GetMapping("/final-exam")
    public ResponseEntity<List<InscriptionToFinalExamResponse>> findAll() {
        return ResponseEntity.ok(inscriptionToFinalExamService.getAllInscriptions());
    }

    @GetMapping("/commission")
    public ResponseEntity<List<InscriptionToCommissionResponse>> findAllInscriptionsCommission(){
        return ResponseEntity.ok(inscriptionToCommissionService.findAll());
    }

    @GetMapping("/final-exam/{id}")
    public ResponseEntity<InscriptionToFinalExamResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(inscriptionToFinalExamService.getInscriptionById(id));
    }

    @GetMapping("/final-exam/commission/{id}")
    public ResponseEntity<InscriptionToCommissionResponse> findCommissionById(@PathVariable Long id) {
        return ResponseEntity.ok(inscriptionToCommissionService.findById(id));
    }

    @GetMapping("/final-exam/date-inscription/{date}")
    public ResponseEntity<List<InscriptionToFinalExamResponse>> findByInscriptionDate(@PathVariable String date) {
        return ResponseEntity.ok(inscriptionToFinalExamService.getAllInscriptionsByInscriptionDate(date));
    }

    @GetMapping("/commission/inscriptions/{dateInscription}")
    public ResponseEntity<List<InscriptionToCommissionResponse>> findByCommissionDate(@PathVariable String dateInscription) {
        return ResponseEntity.ok(inscriptionToCommissionService.findByinscriptionDate(dateInscription));
    }

    @GetMapping("/final-exam/date-exam/{date}")
    public ResponseEntity<List<InscriptionToFinalExamResponse>> findByInscriptionDateExam(@PathVariable String date) {
        return ResponseEntity.ok(inscriptionToFinalExamService.getAllInscriptionsByExamDate(date));
    }

    @GetMapping("/commission/finishDate/{finishDate}")
    public ResponseEntity<List<InscriptionToCommissionResponse>> findByInscriptionDateFinish(@PathVariable String finishDate) {
        return ResponseEntity.ok(inscriptionToCommissionService.findByfinishInscription(finishDate));
    }

    @GetMapping("/final-exam/subjects/{id}")
    public ResponseEntity<List<InscriptionToFinalExamResponse>> findBySubjectId(@PathVariable Long id) {
        return ResponseEntity.ok(inscriptionToFinalExamService.getAllInscriptionsBySubjectId(id));
    }

    @GetMapping("/commission/find/{id}")
    public ResponseEntity<List<InscriptionToCommissionResponse>> findByCommissionId(@PathVariable Long id) {
        return ResponseEntity.ok(inscriptionToCommissionService.findAllByCommissionId(id));
    }

    @GetMapping("/final-exam/final-exams-students/{studentId}")
    public ResponseEntity<List<InscriptionToFinalExamResponse>> findByFinalExamStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(inscriptionToFinalExamService.getActiveInscriptionsForStudent(studentId));
    }

    @GetMapping("/final-exam/teacher/{teacherId}")
    public ResponseEntity<List<InscriptionToFinalExamResponse>> findByTeacherId(@PathVariable Long teacherId) {
        return ResponseEntity.ok(inscriptionToFinalExamService.getInscriptionsByTeacherId(teacherId));
    }

    @GetMapping("/commission/student/{id}")
    public ResponseEntity<List<InscriptionToCommissionResponse>> findByStudentId(@PathVariable Long id) {
        return ResponseEntity.ok(inscriptionToCommissionService.findByCareertoStudent(id));
    }

    @GetMapping("/commission/inscriptionsBeforeFinish")
    public ResponseEntity<List<InscriptionToCommissionResponse>> findByInscriptionsBeforeFinish() {
        return ResponseEntity.ok(inscriptionToCommissionService.findByInscriptionDateBeforeAndFinishInscriptionDateAfter());
    }

    @PostMapping("/final-exam")
    public ResponseEntity<InscriptionToFinalExamDTO> save(@RequestBody InscriptionToFinalExamDTO entity, @RequestParam String teacherLegajo) {
        inscriptionToFinalExamService.createInscription(entity, teacherLegajo);
        return ResponseEntity.ok(entity);
    }

    @PostMapping("/commission")
    public ResponseEntity<?> saveCommission(@RequestBody InscriptionToCommissionDTO entity) {
        inscriptionToCommissionService.createInscription(entity);
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/final-exam/{id}")
    public ResponseEntity<InscriptionToFinalExamDTO> update(@PathVariable Long id, @RequestBody InscriptionToFinalExamDTO entity) {
        inscriptionToFinalExamService.updateInscription(entity, id);
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/commission/{id}")
    public ResponseEntity<?> updateCommission(@PathVariable Long id, @RequestBody InscriptionToCommissionDTO entity) {
        inscriptionToCommissionService.updateInscription(entity, id);
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/final-exam/register-student-for-exam/{id}")
    public ResponseEntity<?> inscriptionAtStudentToExam(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long studentId = userDetails.getId();

        inscriptionToFinalExamService.addToStudent(id, studentId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("final-exam/unregister-student-for-exam/{inscriptionId}")
    public ResponseEntity<?> unregisterStudentForExam(@PathVariable Long inscriptionId, @RequestBody Long studentId) {
        inscriptionToFinalExamService.unregisterStudent(inscriptionId, studentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/final-exam/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inscriptionToFinalExamService.deleteInscription(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/commission/{id}")
    public ResponseEntity<Void> deleteCommission(@PathVariable Long id) {
        inscriptionToCommissionService.deleteInscriptionById(id);
        return ResponseEntity.noContent().build();
    }

}
