package MyAcad.Project.backend.Repository;

import MyAcad.Project.backend.Model.Exam.ExamEntity;
import MyAcad.Project.backend.Model.Subjects.SubjectsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<ExamEntity, Long> {

    List<ExamEntity> findBySubject(SubjectsEntity subject);


    List<ExamEntity> findBySubject_Id(Long subjectId);
}
