package MyAcad.Project.backend.Repository.Users;

import MyAcad.Project.backend.Model.Users.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query("SELECT u FROM #{#entityName} u WHERE u.legajo = :legajo")
    Optional<Teacher> findByLegajo(@Param("legajo") String legajo);

    List<Teacher> findByLegajoContaining(String legajo);

    //Obtener todos los docentes por los id
    List<Teacher> findByIdIn(List<Long> ids);

    //Obtener por comisión
    @Query("SELECT t FROM Commission c JOIN c.teachers t WHERE c.id = :commissionId")
    List<Long> findTeachersByCommissionId(@Param("commissionId") Long commissionId);

    @Query("SELECT t FROM Teacher t WHERE " +
            "LOWER(CONCAT(t.name, ' ', t.lastName)) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Teacher> findByFullName(@Param("term") String term);

    @Query("SELECT u FROM #{#entityName} u WHERE u.email = :email")
    Optional<Teacher> findByEmail(@Param("email")String email);

    @Query("SELECT u FROM #{#entityName} u WHERE u.dni = :dni")
    Optional<Teacher> findByDni(@Param("dni")int dni);

    @Query("""
        SELECT DISTINCT t
        FROM Teacher t
        JOIN t.commissions c
        JOIN t.subjects s
        WHERE c.id = :commissionId
          AND s.id = :subjectId
    """)
    Optional<Teacher> findTeacherByCommissionAndSubject(
            @Param("commissionId") Long commissionId,
            @Param("subjectId") Long subjectId
    );
}
