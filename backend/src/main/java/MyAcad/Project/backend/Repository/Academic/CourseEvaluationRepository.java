package MyAcad.Project.backend.Repository.Academic;

import MyAcad.Project.backend.Model.Academic.CourseEvaluationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface CourseEvaluationRepository extends JpaRepository<CourseEvaluationEntity, Long> {

    List<CourseEvaluationEntity> findBySubject_Id(Long id);
    List<CourseEvaluationEntity> findByTeacher_Id(Long id);
    List<CourseEvaluationEntity> findByCreatedAt(LocalDateTime createdAt);

    Page<CourseEvaluationEntity> findBySubject_Id(
            Long subjectId,
            Pageable pageable
    );

    Page<CourseEvaluationEntity> findByTeacher_Id(
            Long teacherId,
            Pageable pageable
    );

    Page<CourseEvaluationEntity> findByCreatedAt(
            LocalDateTime createdAt,
            Pageable pageable
    );

}
