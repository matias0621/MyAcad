package MyAcad.Project.backend.Repository.Academic;

import MyAcad.Project.backend.Model.Academic.FinalExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinalExamRepository extends JpaRepository<FinalExamEntity, Long> {

    List<FinalExamEntity> findBySubject_Id(Long subjectId);
}
