package MyAcad.Project.backend.Repository;

import MyAcad.Project.backend.Model.Academic.SubjectsXStudentEntity;
import MyAcad.Project.backend.Model.Users.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectsXStudentRepository extends JpaRepository<SubjectsXStudentEntity, Long> {
    List<SubjectsXStudentEntity> findByStudent_Id(Long studentId);

    Optional<SubjectsXStudentEntity> findByStudent_IdAndSubjects_Id(Long studentId, Long subjectId);

    @Query("SELECT sxs.student FROM SubjectsXStudentEntity sxs " +
            "WHERE sxs.subjects.id = :subjectId " +
            "AND sxs.commission.id = :commissionId " +
            "AND sxs.teacher.id = :teacherId")
    List<Student> findStudentsBySubjectAndCommissionAndTeacher(
            @Param("subjectId") Long subjectId,
            @Param("commissionId") Long commissionId,
            @Param("teacherId") Long teacherId
    );
}
