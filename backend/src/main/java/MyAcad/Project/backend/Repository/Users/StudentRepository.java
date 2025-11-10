package MyAcad.Project.backend.Repository.Users;

import MyAcad.Project.backend.Model.Users.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT u FROM #{#entityName} u WHERE u.legajo = :legajo")
    Optional<Student> findByLegajo(@Param("legajo") String legajo);

    List<Student> findByLegajoContaining(String legajo);

    //Obtener todos los estudiantes por los id
    List<Student> findByIdIn(List<Long> ids);

    @Query("SELECT s FROM Commission c JOIN c.students s WHERE c.id = :commissionId")
    List<Long> findStudentsByCommissionId(@Param("commissionId") Long commissionId);


    @Query("SELECT s FROM Student s WHERE " +
            "LOWER(CONCAT(s.name, ' ', s.lastName)) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Student> findByFullName(@Param("term") String term);

    @Query("SELECT u FROM #{#entityName} u WHERE u.email = :email")
    Optional<Student> findByEmail(@Param("email")String email);

    @Query("SELECT u FROM #{#entityName} u WHERE u.dni = :dni")
    Optional<Student> findByDni(@Param("dni")int dni);
}
