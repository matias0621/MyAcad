package MyAcad.Project.backend.Repository.Academic;

import MyAcad.Project.backend.Model.Academic.ExamsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamsRepository extends JpaRepository<ExamsEntity, Long> {

    List<ExamsEntity> findBySubject_Id(Long subjectId);
    List<ExamsEntity> findAllByStudent_Id(Long studentId);
    List<ExamsEntity> findAllBySubject_IdAndStudent_Id(Long subjectId, Long studentId);
}
