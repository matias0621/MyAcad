package MyAcad.Project.backend.Service.Users;

import MyAcad.Project.backend.Configuration.SecurityConfig;
import MyAcad.Project.backend.Enum.Role;
import MyAcad.Project.backend.Exception.DniAlreadyExistsException;
import MyAcad.Project.backend.Exception.EmailAlreadyExistsException;
import MyAcad.Project.backend.Exception.LegajoAlreadyExistsException;
import MyAcad.Project.backend.Model.Programs.Program;

import MyAcad.Project.backend.Mapper.ProgramMapper;
import MyAcad.Project.backend.Model.Programs.ProgramResponse;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.StudentCsvDto;
import MyAcad.Project.backend.Repository.Programs.ProgramRepository;

import MyAcad.Project.backend.Model.Programs.ProgramResponse;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.StudentCsvDto;

import MyAcad.Project.backend.Model.Users.StudentResponse;
import MyAcad.Project.backend.Repository.Users.StudentRepository;
import MyAcad.Project.backend.Service.Programs.ProgramService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository repository;
     private final ProgramMapper programMapper;
    private final ProgramRepository programRepository;
    private final UserLookupService userLookupService;
    private final ProgramService programService;
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
            student.setLastName(record.getLastname());
            student.setRole(Role.STUDENT);
            add(student);
        }
    }

    public Page<StudentResponse> listStudentsPaginated(int page, int size) {
        Page<Student> list = repository.findAll(PageRequest.of(page, size));
        return list.map(this::toResponse);
    }


    public Page<StudentResponse> listStudentResponsePaginated(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        // Traés los estudiantes paginados
        Page<Student> studentsPage = repository.findAll(pageable);

        // Transformás el contenido a StudentResponse
        List<StudentResponse> responseList = studentsPage.getContent().stream()
                .map(student -> StudentResponse.builder()
                        .name(student.getName())
                        .lastName(student.getLastName())
                        .id(student.getId())
                        .dni(student.getDni())
                        .email(student.getEmail())
                        .legajo(student.getLegajo())
                        .programs(programMapper.toResponseList(
                                programRepository.findByStudent(student.getId())
                        ))
                        .build()
                )
                .toList();

        // Devolvés un Page nuevo con tus respuestas ya mapeadas
        return new PageImpl<>(
                responseList,
                pageable,
                studentsPage.getTotalElements()
        );
    }


    public List<StudentResponse> getByCommission(Long commissionId) {

        List<Long> studentIds = repository.findStudentsByCommissionId(commissionId);
        List<Student> listOfStudent = repository.findByIdIn(studentIds);
        List<StudentResponse> responses = new ArrayList<>();
        for (Student student : listOfStudent) {
            responses.add(toResponse(student));
        }
        return responses;
    }


    public List<StudentResponse> getByLegajoContaining(String legajo) {
        List<Student> list = repository.findByLegajoContaining(legajo);
        List<StudentResponse> responses = new ArrayList<>();
        for (Student student : list) {
            responses.add(toResponse(student));
        }
        return responses;
    }

    public List<StudentResponse> getByFullName(String fullName) {
        List<Student> list = repository.findByFullName(fullName);
        List<StudentResponse> responses = new ArrayList<>();
        for (Student student : list) {
            responses.add(toResponse(student));
        }
        return responses;
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

    public List<StudentResponse> list() {
        List<Student> list = repository.findAll();
        List<StudentResponse> responses = new ArrayList<>();
        for (Student student : list) {
            responses.add(toResponse(student));
        }
        return responses;
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

    public Optional<StudentResponse> getById(Long id){
        Optional<Student> student = repository.findById(id);
        return student.map(this::toResponse);
    }

    public Optional<StudentResponse> getByLegajo(String legajo) {
        Optional<Student> student = repository.findByLegajo(legajo);
        return student.map(this::toResponse);
    }

    public Optional<StudentResponse> getByEmail(String email){
        Optional<Student> student = repository.findByEmail(email);
        return student.map(this::toResponse);
    }

    private StudentResponse toResponse(Student student){
        List<ProgramResponse> programs = programService.findByStudent(student.getId());

        return StudentResponse.builder()
                .name(student.getName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .dni(student.getDni())
                .isActive(student.isActive())
                .programs(programs)
                .id(student.getId())
                .role(student.getRole())
                .legajo(student.getLegajo())
                .build();
    }
}
