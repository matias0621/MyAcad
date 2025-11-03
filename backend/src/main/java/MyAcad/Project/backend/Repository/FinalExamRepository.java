package MyAcad.Project.backend.Repository;

import MyAcad.Project.backend.Model.Exam.ExamEntity;
import MyAcad.Project.backend.Model.FinalExam.FinalExamEntity;
import MyAcad.Project.backend.Model.Users.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinalExamRepository extends JpaRepository<FinalExamEntity, Long> {

    List<FinalExamEntity> findBySubject_Id(Long subjectId);
}
