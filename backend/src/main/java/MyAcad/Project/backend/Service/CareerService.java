package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Exception.CareerAlreadyExistsException;
import MyAcad.Project.backend.Model.Users.Career;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Repository.CareerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CareerService {
    private final CareerRepository repository;

    public void add(Career c) {
        if (repository.findCareerByName(c.getName()).isPresent()) {
            throw new RuntimeException("Career name already exists");
        }
        repository.save(c);
    }

    public Page<Career> listCareersPaginated(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public List<Career> getByNameIgnoringCase(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    public ResponseEntity<Void> delete(Long id){
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public List<Career> list() {
        return repository.findAll();
    }

    public void modify(Long id, Career c) {
        Optional<Career> optionalOld = repository.findById(id);
        if (optionalOld.isEmpty()) {
            throw new RuntimeException("Career not found");
        }
        Career old = optionalOld.get();
        if (!old.getName().equals(c.getName())) {
            if (repository.findCareerByName(c.getName()).isPresent()) {
                throw new CareerAlreadyExistsException();
            }
            old.setName(c.getName());
        }
        old.setDescription(c.getDescription());
        old.setDurationMonths(c.getDurationMonths());
        old.setMonthlyFee(c.getMonthlyFee());
        old.setAnnualFee(c.getAnnualFee());
        old.setActive(c.getActive());

        repository.save(old);
    }

    public Optional<Career> getById(Long id){
        return repository.findById(id);
    }

    public Optional<Career> getByName(String name) {
        return Optional.ofNullable(repository.findByName(name));
    }

}
