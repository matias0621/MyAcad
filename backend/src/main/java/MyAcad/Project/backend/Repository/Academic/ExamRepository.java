package MyAcad.Project.backend.Repository.Academic;

import MyAcad.Project.backend.Model.Academic.ExamEntity;
import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<ExamEntity, Long> {

    List<ExamEntity> findBySubject(SubjectsEntity subject);


    List<ExamEntity> findBySubject_Id(Long subjectId);
}
