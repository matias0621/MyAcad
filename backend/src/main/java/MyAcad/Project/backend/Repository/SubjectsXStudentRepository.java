package MyAcad.Project.backend.Repository;

import MyAcad.Project.backend.Model.MateriaXAlumno.SubjectsXStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectsXStudentRepository extends JpaRepository<SubjectsXStudentEntity, Long> {
    List<SubjectsXStudentEntity> findByStudent_Id(Long studentId);

    Optional<SubjectsXStudentEntity> findByStudent_IdAndSubjects_Id(Long studentId, Long subjectId);
}
