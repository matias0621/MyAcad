package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Model.Commission.Commission;
import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Repository.CommissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommissionService {
    private final CommissionRepository repository;

    public void add(Commission c) {
        if (repository.findCommissionByNumberAndProgram(c.getNumber(), c.getProgram()).isPresent()) {
            throw new RuntimeException("Commission number already exists");
        }
        repository.save(c);
    }

    public Page<Commission> listCommissionPaginated(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public ResponseEntity<Void> delete(Long id) {
        Optional<Commission> optionalCommission = repository.findById(id);
        if (optionalCommission.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Commission c = optionalCommission.get();
        c.setActive(false);
        repository.save(c);
        return ResponseEntity.noContent().build();
    }

    public List<Commission> list() {
        return repository.findAll();
    }
    public List<Commission> listActive() {
        return repository.findAllActives();
    }

    public void modify(Long id, Commission c) {
        Optional<Commission> optionalOld = repository.findById(id);
        if (optionalOld.isEmpty()) {
            throw new RuntimeException("Commission not found");
        }
        Commission old = optionalOld.get();
        if (old.getNumber() != c.getNumber()) {
            if (repository.findCommissionByNumberAndProgram(c.getNumber(), c.getProgram()).isPresent()) {
                throw new RuntimeException("Commission number already exists");
            }
        }

        old.setNumber(c.getNumber());
        old.setSubjects(c.getSubjects());
        old.setStudents(c.getStudents());
        old.setProgram(c.getProgram());
        old.setCapacity(c.getCapacity());
        old.setActive(c.isActive());

        repository.save(old);
    }

    public Optional<Commission> getById(Long id){
        return repository.findById(id);
    }

    public List<Commission> findByProgram(String program) {
        return repository.findByProgram(program);
    }

    public Optional<Optional<Commission>> getByNumber(int number) {
        return Optional.ofNullable(repository.findByNumber(number));
    }

}
