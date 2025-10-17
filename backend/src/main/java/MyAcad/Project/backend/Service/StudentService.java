package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Configuration.SecurityConfig;
import MyAcad.Project.backend.Exception.EmailAlreadyExistsException;
import MyAcad.Project.backend.Exception.UsernameAlreadyExistsException;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Repository.StudentRepository;
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
public class StudentService {
    private final StudentRepository repository;
    private final UserLookupService userLookupService;
    private PasswordEncoder passwordEncoder;

    public void add(Student t) {
        if (userLookupService.findByUsername(t.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }else if(userLookupService.findByEmail(t.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        t.setPassword(SecurityConfig.passwordEncoder().encode(t.getPassword()));
        repository.save(t);
    }

    public Page<Student> listStudentsPaginated(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public List<Student> getByUsernameIgnoringCase(String username) {
        return repository.findByUsernameContainingIgnoreCase(username);
    }

    public ResponseEntity<Void> delete(Long id){
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public List<Student> list() {
        return repository.findAll();
    }

    public void modify (Long id, Student t){
        Student old = repository.findById(id).get();
        //Verificar si cambió el usuario o el email
        if (!old.getEmail().equals(t.getEmail())) {
            //Verificar si el email nuevo ya se encuentra en uso
            if (userLookupService.findByEmail(t.getEmail()).isPresent()) {
                throw new EmailAlreadyExistsException();
            }
        }else if (!old.getUsername().equals(t.getUsername())) {
            //Verificar si el usuario nuevo ya se encuentra en uso
            if (userLookupService.findByUsername(t.getUsername()).isPresent()) {
                throw new UsernameAlreadyExistsException();
            }
        }
        old.setName(t.getName());
        old.setLastName(t.getLastName());
        old.setEmail(t.getEmail());
        old.setUsername(t.getUsername());
        old.setLegajo(t.getLegajo());

        // Verificar si se ingresó una contraseña nueva, si el usuario no quiso cambiarla debe dejar ese input vacío.
        if (t.getPassword() != null && !t.getPassword().isBlank()) {
            String encoded = passwordEncoder.encode(t.getPassword());
            old.setPassword(encoded);
        }
        repository.save(old);
    }

    public Optional<Student> getById(Long id){
        return repository.findById(id);
    }

    public Optional<Student> getByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Optional<Student> getByEmail(String email){
        return repository.findByEmail(email);
    }
}
