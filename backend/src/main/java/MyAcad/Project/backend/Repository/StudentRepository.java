package MyAcad.Project.backend.Repository;

import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT u FROM #{#entityName} u WHERE u.username = :username")
    Optional<Student> findByUsername(@Param("username") String username);

    List<Student> findByUsernameContainingIgnoreCase(String username);

    @Query("SELECT u FROM #{#entityName} u WHERE u.email = :email")
    Optional<Student> findByEmail(@Param("email")String email);
}
