package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Configuration.SecurityConfig;
import MyAcad.Project.backend.Exception.EmailAlreadyExistsException;
import MyAcad.Project.backend.Exception.LegajoAlreadyExistsException;
import MyAcad.Project.backend.Model.Users.Manager;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Repository.ManagerRepository;
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
public class ManagerService {
    private final ManagerRepository repository;
    private final UserLookupService userLookupService;
    private PasswordEncoder passwordEncoder;

    public void add(Manager t) {
        if (userLookupService.findByLegajo(t.getLegajo()).isPresent()) {
            throw new LegajoAlreadyExistsException();
        }else if(userLookupService.findByEmail(t.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        t.setPassword(SecurityConfig.passwordEncoder().encode(t.getPassword()));
        t = repository.save(t);
        t.setLegajo(String.valueOf(t.getId() + 900000));

        repository.save(t);
    }

    public Page<Manager> listManagersPaginated(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public List<Manager> getByLegajoContaining(String legajo) {
        return repository.findByLegajoContaining(legajo);
    }

    public List<Manager> getByFullName(String fullName) {
        return repository.findByFullName(fullName);
    }
    public ResponseEntity<Void> delete(Long id){
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public List<Manager> list() {
        return repository.findAll();
    }

    public void modify (Long id, Manager t){
        Manager old = repository.findById(id).get();
        //Verificar si cambió el legajo o el email
        if (!old.getEmail().equals(t.getEmail())) {
            //Verificar si el email nuevo ya se encuentra en uso
            if (userLookupService.findByEmail(t.getEmail()).isPresent()) {
                throw new EmailAlreadyExistsException();
            }
        }else if (old.getLegajo() != t.getLegajo()) {
            //Verificar si el legajo nuevo ya se encuentra en uso
            if (userLookupService.findByLegajo(t.getLegajo()).isPresent()) {
                throw new LegajoAlreadyExistsException();
            }
        }
        old.setName(t.getName());
        old.setLastName(t.getLastName());
        old.setEmail(t.getEmail());

        // Verificar si se ingresó una contraseña nueva, si el usuario no quiso cambiarla debe dejar ese input vacío.
        if (t.getPassword() != null && !t.getPassword().isBlank()) {
            String encoded = passwordEncoder.encode(t.getPassword());
            old.setPassword(encoded);
        }
        repository.save(old);
    }

    public Optional<Manager> getById(Long id){
        return repository.findById(id);
    }

    public Optional<Manager> getByLegajo(String legajo) {
        return repository.findByLegajo(legajo);
    }

    public Optional<Manager> getByEmail(String email){
        return repository.findByEmail(email);
    }
}
