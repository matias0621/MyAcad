package MyAcad.Project.backend.Service.Users;

import MyAcad.Project.backend.Configuration.SecurityConfig;
import MyAcad.Project.backend.Enum.Role;
import MyAcad.Project.backend.Exception.DniAlreadyExistsException;
import MyAcad.Project.backend.Exception.EmailAlreadyExistsException;
import MyAcad.Project.backend.Exception.LegajoAlreadyExistsException;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.StudentCsvDto;
import MyAcad.Project.backend.Repository.Users.StudentRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository repository;
    private final UserLookupService userLookupService;
    private PasswordEncoder passwordEncoder;

    public void add(Student t) {
        if (userLookupService.findByLegajo(t.getLegajo()).isPresent()) {
            throw new LegajoAlreadyExistsException();
        }else if(userLookupService.findByEmail(t.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }else if(userLookupService.findByDni(t.getDni()).isPresent()) {
            throw new DniAlreadyExistsException();
        }

        t.setPassword(SecurityConfig.passwordEncoder().encode(t.getPassword()));

        t = repository.save(t);
        t.setLegajo(String.valueOf(t.getId() + 100000));

        repository.save(t);
    }

    public List<StudentCsvDto> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<StudentCsvDto> csvToBean = new CsvToBeanBuilder<StudentCsvDto>(reader)
                    .withType(StudentCsvDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        }
    }

    public void saveStudentByCsv(List<StudentCsvDto> records){
        for (StudentCsvDto record : records) {
            Student student = new Student();
            student.setEmail(record.getEmail());
            student.setPassword(String.valueOf(record.getDni()));
            student.setActive(true);
            student.setDni(Integer.parseInt((record.getDni())));
            student.setName(record.getName());
            student.setLastName(student.getLastName());
            student.setRole(Role.STUDENT);
            add(student);
        }
    }

    public Page<Student> listStudentsPaginated(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public List<Student> getByCommission(Long commissionId) {
        List<Long> studentIds = repository.findStudentsByCommissionId(commissionId);
        return repository.findByIdIn(studentIds);
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

    public void modify (Long id, Student t){
        Student old = repository.findById(id).get();
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

    public Optional<Student> getById(Long id){
        return repository.findById(id);
    }

    public Optional<Student> getByLegajo(String legajo) {
        return repository.findByLegajo(legajo);
    }

    public Optional<Student> getByEmail(String email){
        return repository.findByEmail(email);
    }
}
