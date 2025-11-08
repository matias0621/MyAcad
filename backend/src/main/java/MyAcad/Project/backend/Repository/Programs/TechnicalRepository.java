package MyAcad.Project.backend.Repository.Programs;

import MyAcad.Project.backend.Model.Programs.Career;
import MyAcad.Project.backend.Model.Programs.Technical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TechnicalRepository extends JpaRepository<Technical, Long> {
    @Query("SELECT c FROM #{#entityName} c WHERE c.name = :name")
    Optional<Career> findByName(String name);

    List<Technical> findByNameContainingIgnoreCase(String name);

    @Query("SELECT c FROM #{#entityName} c WHERE c.active = true")
    Optional<Technical> findByActiveTrue(@Param("active") boolean active);

    Optional<Technical> findTechnicalByName(String name);

    //Buscar tecnicatura por estudiante o profesor
    @Query("SELECT c FROM Technical c JOIN c.students s WHERE s.id = :studentId")
    List<Technical> findByStudent(@Param("studentId") Long studentId);

    @Query("SELECT c FROM Technical c JOIN c.teachers t WHERE t.id = :teacherId")
    List<Technical> findByTeacher(@Param("teacherId") Long teacherId);
}