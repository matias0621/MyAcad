package MyAcad.Project.backend.Service.Users;

import MyAcad.Project.backend.Exception.DniAlreadyExistsException;
import MyAcad.Project.backend.Exception.EmailAlreadyExistsException;
import MyAcad.Project.backend.Exception.LegajoAlreadyExistsException;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Repository.SubjectsXStudentRepository;
import MyAcad.Project.backend.Repository.Users.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository repository;
    private final UserLookupService userLookupService;
    private final PasswordEncoder passwordEncoder;
    private final SubjectsXStudentRepository subjectsXStudentRepository;

    public void add(Student t) {
        if (userLookupService.findByLegajo(t.getLegajo()).isPresent()) {
            throw new LegajoAlreadyExistsException();
        } else if (userLookupService.findByEmail(t.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        } else if (userLookupService.findByDni(t.getDni()).isPresent()) {
            throw new DniAlreadyExistsException();
        }

        // usar SIEMPRE el bean inyectado
        t.setPassword(passwordEncoder.encode(t.getPassword()));

        t = repository.save(t);
        t.setLegajo(String.valueOf(t.getId() + 100000));

        repository.save(t);
    }

    public Page<Student> listStudentsPaginated(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public List<Student> getByLegajoContaining(String legajo) {
        return repository.findByLegajoContaining(legajo);
    }

    public List<Student> getByFullName(String fullName) {
        return repository.findByFullName(fullName);
    }

    public ResponseEntity<Void> delete(Long id){
        Optional<Student> optionalStudent = repository.findById(id);
        if (optionalStudent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Student student = optionalStudent.get();
        student.setActive(false);
        repository.save(student);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> definitiveDeleteStudent(Long studentId) {
        if (!repository.existsById(studentId)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(studentId);
        return ResponseEntity.ok().build();
    }

    public List<Student> list() {
        return repository.findAll();
    }

    public void modify(Long id, Student t){
        Student old = repository.findById(id).orElseThrow();

        if (!old.getEmail().equals(t.getEmail())) {
            if (userLookupService.findByEmail(t.getEmail()).isPresent()) {
                throw new EmailAlreadyExistsException();
            }
        }

        old.setName(t.getName());
        old.setLastName(t.getLastName());
        old.setEmail(t.getEmail());
        old.setDni(t.getDni());
        old.setActive(t.isActive());

        if (t.getPassword() != null && !t.getPassword().isBlank()) {
            String encoded = passwordEncoder.encode(t.getPassword());
            old.setPassword(encoded);
        }

        repository.save(old);
    }

    public Optional<Student> getById(Long id){
        return repository.findById(id);
    }

    public Optional<Student> getByLegajo(String legajo) {
        return repository.findByLegajo(legajo);
    }

    public Optional<Student> getByEmail(String email){
        return repository.findByEmail(email);
    }

    public List<Student> getStudentsForTeacherSubjectCommission(Long teacherId, Long subjectId, Long commissionId) {
        return subjectsXStudentRepository.findStudentsBySubjectAndCommissionAndTeacher(subjectId, commissionId, teacherId);
    }
}
