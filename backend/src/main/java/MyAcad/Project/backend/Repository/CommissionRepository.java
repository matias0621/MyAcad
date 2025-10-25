package MyAcad.Project.backend.Repository;

import MyAcad.Project.backend.Model.Commission.Commission;
import MyAcad.Project.backend.Model.Programs.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommissionRepository extends JpaRepository<Commission, Long> {

    @Query("SELECT c FROM #{#entityName} c WHERE c.number = :number")
    Optional<Commission> findByNumber(@Param("number") int number);


    @Query("SELECT c FROM #{#entityName} c WHERE c.active = true")
    List<Commission> findAllByActiveTrue();

    Optional<Commission> findFirstByActiveTrue();

    Optional<Object> findCommissionByNumber(int number);
}
