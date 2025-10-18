package MyAcad.Project.backend.Repository;

import MyAcad.Project.backend.Model.Users.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    @Query("SELECT u FROM #{#entityName} u WHERE u.username = :username")
    Optional<Manager> findByUsername(@Param("username") String username);

    List<Manager> findByUsernameContainingIgnoreCase(String username);

    @Query("SELECT u FROM #{#entityName} u WHERE u.email = :email")
    Optional<Manager> findByEmail(@Param("email")String email);
}
