package MyAcad.Project.backend.Service.Users;

import MyAcad.Project.backend.Configuration.SecurityConfig;
import MyAcad.Project.backend.Exception.DniAlreadyExistsException;
import MyAcad.Project.backend.Exception.EmailAlreadyExistsException;
import MyAcad.Project.backend.Exception.LegajoAlreadyExistsException;
import MyAcad.Project.backend.Mapper.TeacherMapper;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Model.Users.TeacherResponse;
import MyAcad.Project.backend.Repository.Users.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final TeacherMapper mapper;
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

    public Page<TeacherResponse> listTeachersPaginated(int page, int size) {
        Page<Teacher> teacherPage = repository.findAll(PageRequest.of(page, size));
        List<TeacherResponse> responseList = teacherPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return new PageImpl<>(
                responseList,
                teacherPage.getPageable(),
                teacherPage.getTotalElements()
        );
    }

    public List<TeacherResponse> getByCommission(Long commissionId) {
        List<Long> studentIds = repository.findTeachersByCommissionId(commissionId);
        return mapper.toResponseList(repository.findByIdIn(studentIds));
    }


    public List<TeacherResponse> getByLegajoContaining(String legajo) {
        return mapper.toResponseList(repository.findByLegajoContaining(legajo));
    }

    public List<TeacherResponse> getByFullName(String fullName) {

        return mapper.toResponseList(repository.findByFullName(fullName));
    }

    public ResponseEntity<Void> delete(Long id){
        Optional<Teacher> optionalTeacher = repository.findById(id);
        if (optionalTeacher.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Teacher teacher = optionalTeacher.get();
        teacher.setActive(false);
        repository.save(teacher);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> definitiveDeleteTeacher(Long teacherId) {
        if (!repository.existsById(teacherId)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(teacherId);
        return ResponseEntity.ok().build();
    }
    public List<TeacherResponse> list() {
        return mapper.toResponseList(repository.findAll());
    }

    public void modify (Long id, Teacher t){
        Teacher old = repository.findById(id).get();
        //Verificar si cambió el legajo o el email
        if (!old.getEmail().equals(t.getEmail())) {
            //Verificar si el email nuevo ya se encuentra en uso
            if (userLookupService.findByEmail(t.getEmail()).isPresent()) {
                throw new EmailAlreadyExistsException();
            }
        }
        old.setName(t.getName());
        old.setLastName(t.getLastName());
        old.setEmail(t.getEmail());
        old.setDni(t.getDni());
        old.setActive(t.isActive());

        // Verificar si se ingresó una contraseña nueva, si el usuario no quiso cambiarla debe dejar ese input vacío.
        if (t.getPassword() != null && !t.getPassword().isBlank()) {
            String encoded = passwordEncoder.encode(t.getPassword());
            old.setPassword(encoded);
        }
        repository.save(old);
    }

    public Optional<TeacherResponse> getById(Long id){
        return repository.findById(id).map(mapper::toResponse);
    }

    public Optional<TeacherResponse> getByLegajo(String legajo) {
        return repository.findByLegajo(legajo).map(mapper::toResponse);
    }

    public Optional<TeacherResponse> getByEmail(String email){
        return repository.findByEmail(email).map(mapper::toResponse);
    }
}
