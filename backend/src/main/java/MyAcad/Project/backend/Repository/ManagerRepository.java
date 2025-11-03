package MyAcad.Project.backend.Repository;

import MyAcad.Project.backend.Model.Users.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    @Query("SELECT u FROM #{#entityName} u WHERE u.legajo = :legajo")
    Optional<Manager> findByLegajo(@Param("legajo") String legajo);

    List<Manager> findByLegajoContaining(String legajo);

    @Query("SELECT m FROM Manager m WHERE " +
            "LOWER(CONCAT(m.name, ' ', m.lastName)) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Manager> findByFullName(@Param("term") String term);


    @Query("SELECT u FROM #{#entityName} u WHERE u.email = :email")
    Optional<Manager> findByEmail(@Param("email")String email);

    @Query("SELECT u FROM #{#entityName} u WHERE u.dni = :dni")
    Optional<Manager> findByDni(@Param("dni")int dni);
}
