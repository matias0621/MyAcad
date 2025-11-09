package MyAcad.Project.backend.Repository.Academic;

import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectsRepository extends JpaRepository<SubjectsEntity, Long> {

    @Query("SELECT u FROM #{#entityName} u WHERE u.name = :name")
    Optional<SubjectsEntity> findByName(@Param("name") String name);

    List<SubjectsEntity> findByProgram(String program);

    List<SubjectsEntity> findBySemestersLessThan(int semester);

    List<SubjectsEntity> findByNameContainingIgnoreCase(String name);

    Page<SubjectsEntity> findBySubjectActiveTrue(Pageable pageable);

    List<SubjectsEntity> findByProgramAndSemestersLessThan(String program, int semester);

}
