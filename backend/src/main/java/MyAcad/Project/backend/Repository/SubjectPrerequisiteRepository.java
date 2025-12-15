package MyAcad.Project.backend.Repository;

import MyAcad.Project.backend.Enum.AcademicStatus;
import MyAcad.Project.backend.Model.Academic.SubjectPrerequisiteEntity;
import MyAcad.Project.backend.Model.Programs.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.security.auth.Subject;
import java.util.List;
import java.util.Optional;

public interface SubjectPrerequisiteRepository extends JpaRepository<SubjectPrerequisiteEntity, Long> {
    List<SubjectPrerequisiteEntity> findBySubject_Id(Long subjectId);
    List<SubjectPrerequisiteEntity> findByPrerequisite_Id(Long prerequisiteId);
    boolean existsBySubject_IdAndPrerequisite_Id(Long subjectId, Long prerequisiteId);
    Optional<SubjectPrerequisiteEntity> findBySubject_IdAndPrerequisite_Id(Long subjectId, Long prerequisiteId);
    List<SubjectPrerequisiteEntity> findByRequiredStatus(AcademicStatus requiredStatus);


    @Query("""
    SELECT sp\s
    FROM SubjectPrerequisiteEntity sp
    WHERE sp.subject.program = :program
   \s""")
    List<SubjectPrerequisiteEntity> findByProgram(@Param("program") String program);

    @Query("""
    SELECT sp.subject
    FROM SubjectPrerequisiteEntity sp
    WHERE sp.prerequisite.id = :prerequisiteId
    AND sp.requiredStatus = :status
    """)
    List<SubjectPrerequisiteEntity> findSubjectsRequiringStatus(@Param("program") String program, @Param("status") AcademicStatus status);
}
