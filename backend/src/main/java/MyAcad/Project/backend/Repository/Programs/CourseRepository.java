package MyAcad.Project.backend.Repository.Programs;

import MyAcad.Project.backend.Model.Programs.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM #{#entityName} c WHERE c.name = :name")
    Course findByName(String name);

    List<Course> findByNameContainingIgnoreCase(String name);

    @Query("SELECT c FROM #{#entityName} c WHERE c.active = true")
    List<Course> findByActiveTrue(@Param("active") boolean active);

    Optional<Course> findCourseByName(String name);

    //Buscar cursos por estudiante o profesor
    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId")
    List<Course> findByStudent(@Param("studentId") Long studentId);

    @Query("SELECT c FROM Course c JOIN c.teachers t WHERE t.id = :teacherId")
    List<Course> findByTeacher(@Param("teacherId") Long teacherId);
}
