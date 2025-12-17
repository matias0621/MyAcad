package MyAcad.Project.backend.Repository.Academic;

import MyAcad.Project.backend.Model.Academic.ExamsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExamsRepository extends JpaRepository<ExamsEntity, Long> {

    List<ExamsEntity> findBySubject_Id(Long subjectId);
    List<ExamsEntity> findAllByStudent_Id(Long studentId);
    List<ExamsEntity> findAllBySubject_IdAndStudent_Id(Long subjectId, Long studentId);

    @Query("SELECT e FROM ExamsEntity e WHERE e.student.id = :studentId AND e.subject.program = :program")
    List<ExamsEntity> findAllByStudent_IdAndProgram(@Param("studentId") Long studentId, @Param("program")String program);
}
