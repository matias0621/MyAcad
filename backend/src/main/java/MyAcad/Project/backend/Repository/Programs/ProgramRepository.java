package MyAcad.Project.backend.Repository.Programs;

import MyAcad.Project.backend.Enum.ProgramType;
import MyAcad.Project.backend.Model.Programs.Career;
import MyAcad.Project.backend.Model.Programs.Course;
import MyAcad.Project.backend.Model.Programs.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    @Query("SELECT c FROM #{#entityName} c WHERE c.name = :name")
    Optional<Program> findByName(String name);

    List<Program> findByNameContainingIgnoreCase(String name);

    @Query("SELECT c FROM #{#entityName} c WHERE c.active = true")
    Optional<Program> findByActiveTrue(@Param("active") boolean active);

    @Query("SELECT c FROM Career c JOIN c.students s WHERE s.id = :studentId")
    List<Program> findByStudent(@Param("studentId") Long studentId);

    @Query("SELECT c FROM Career c JOIN c.teachers t WHERE t.id = :teacherId")
    List<Program> findByTeacher(@Param("teacherId") Long teacherId);

    List<Program> findByProgramType(ProgramType programType);
}
