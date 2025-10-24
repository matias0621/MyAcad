package MyAcad.Project.backend.Repository;

import MyAcad.Project.backend.Model.Users.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query("SELECT u FROM #{#entityName} u WHERE u.legajo = :legajo")
    Optional<Teacher> findByLegajo(@Param("legajo") String legajo);

    List<Teacher> findByLegajoContainingIgnoreCase(String username);

    @Query("SELECT u FROM #{#entityName} u WHERE u.email = :email")
    Optional<Teacher> findByEmail(@Param("email")String email);
}
