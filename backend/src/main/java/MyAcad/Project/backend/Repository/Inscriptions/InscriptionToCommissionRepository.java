package MyAcad.Project.backend.Repository.Inscriptions;

import MyAcad.Project.backend.Model.Academic.Commission;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission.InscriptionToCommissionEntity;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToFinalExam.InscriptionToFinalExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InscriptionToCommissionRepository extends JpaRepository<InscriptionToCommissionEntity, Long> {

    List<InscriptionToCommissionEntity> findInscriptionToFinalExamEntitiesByInscriptionDate(LocalDateTime inscriptionDate);
    List<InscriptionToCommissionEntity> findInscriptionToFinalExamEntitiesByFinishInscriptionDate(LocalDateTime finishInscriptionDate);
    List<InscriptionToCommissionEntity> findByInscriptionDateBeforeAndFinishInscriptionDateAfter(
            LocalDateTime now1,
            LocalDateTime now2
    );
    List<InscriptionToCommissionEntity> findByCommission(Commission commission);

    @Query("""
    SELECT i
    FROM InscriptionToCommissionEntity i
    WHERE i.inscriptionDate <= :now
      AND i.finishInscriptionDate > :now
      AND i.commission.program IN (
            SELECT p.name
            FROM Program p
            JOIN p.students s
            WHERE s.id = :studentId
      )
""")
    List<InscriptionToCommissionEntity> findActiveInscriptionsByStudentProgram(
            @Param("studentId") Long studentId,
            @Param("now") LocalDateTime now
    );


}
