package MyAcad.Project.backend.Repository.Programs;

import MyAcad.Project.backend.Model.Programs.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CareerRepository extends JpaRepository<Career, Long> {
    @Query("SELECT c FROM #{#entityName} c WHERE c.name = :name")
    Optional<Career> findByName(String name);

    List<Career> findByNameContainingIgnoreCase(String name);

    @Query("SELECT c FROM #{#entityName} c WHERE c.active = true")
    Optional<Career> findByActiveTrue(@Param("active") boolean active);

    Optional<Object> findCareerByName(String name);
}
