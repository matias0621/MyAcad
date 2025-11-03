package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Configuration.SecurityConfig;
import MyAcad.Project.backend.Exception.DniAlreadyExistsException;
import MyAcad.Project.backend.Exception.EmailAlreadyExistsException;
import MyAcad.Project.backend.Exception.LegajoAlreadyExistsException;
import MyAcad.Project.backend.Model.Users.Manager;
import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Repository.TeacherRepository;
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
public class TeacherService {
    private final TeacherRepository repository;
    private final UserLookupService userLookupService;
    private PasswordEncoder passwordEncoder;

    public void add(Teacher t) {
        if (userLookupService.findByLegajo(t.getLegajo()).isPresent()) {
            throw new LegajoAlreadyExistsException();
        }else if(userLookupService.findByEmail(t.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }else if(userLookupService.findByDni(t.getDni()).isPresent()) {
            throw new DniAlreadyExistsException();
        }

        t.setPassword(SecurityConfig.passwordEncoder().encode(t.getPassword()));
        t = repository.save(t);
        t.setLegajo(String.valueOf(t.getId() + 600000));

        repository.save(t);
    }

    public Page<Teacher> listTeachersPaginated(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public List<Teacher> getByLegajoContaining(String legajo) {
        return repository.findByLegajoContaining(legajo);
    }

    public List<Teacher> getByFullName(String fullName) {
        return repository.findByFullName(fullName);
    }

    public ResponseEntity<Void> delete(Long id){
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public List<Teacher> list() {
        return repository.findAll();
    }

    public void modify (Long id, Teacher t){
        Teacher old = repository.findById(id).get();
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
        old.setLegajo(t.getLegajo());
        old.setDni(t.getDni());

        // Verificar si se ingresó una contraseña nueva, si el usuario no quiso cambiarla debe dejar ese input vacío.
        if (t.getPassword() != null && !t.getPassword().isBlank()) {
            String encoded = passwordEncoder.encode(t.getPassword());
            old.setPassword(encoded);
        }
        repository.save(old);
    }

    public Optional<Teacher> getById(Long id){
        return repository.findById(id);
    }

    public Optional<Teacher> getByLegajo(String legajo) {
        return repository.findByLegajo(legajo);
    }

    public Optional<Teacher> getByEmail(String email){
        return repository.findByEmail(email);
    }
}
