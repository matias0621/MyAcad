package MyAcad.Project.backend.Repository;

import MyAcad.Project.backend.Model.InscriptionToFinalExam.InscriptionToFinalExamEntity;
import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.security.auth.Subject;
import java.time.LocalDateTime;
import java.util.List;

public interface InscriptionToFinalExamRepository extends JpaRepository<InscriptionToFinalExamEntity, Long> {

    List<InscriptionToFinalExamEntity> findInscriptionToFinalExamEntitiesBySubjects(SubjectsEntity subject);

    List<InscriptionToFinalExamEntity> findInscriptionToFinalExamEntitiesByInscriptionDate(LocalDateTime inscriptionDate);

    List<InscriptionToFinalExamEntity> findInscriptionToFinalExamEntitiesByFinalExamDate(LocalDateTime finalExamDate);

    @Query("""
    SELECT i
    FROM InscriptionToFinalExamEntity i
    WHERE i.program.id IN (
        SELECT p.id FROM Program p
        JOIN p.students s
        WHERE s.id = :studentId
    )
    AND i.inscriptionDate <= :now
    AND i.finalExamDate >= :now
    """)
    List<InscriptionToFinalExamEntity> findActiveInscriptionsForStudent(Long studentId, LocalDateTime now);

}
