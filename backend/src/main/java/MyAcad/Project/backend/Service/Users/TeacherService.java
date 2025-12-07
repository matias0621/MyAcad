package MyAcad.Project.backend.Service.Users;

import MyAcad.Project.backend.Configuration.SecurityConfig;
import MyAcad.Project.backend.Enum.Role;
import MyAcad.Project.backend.Exception.DniAlreadyExistsException;
import MyAcad.Project.backend.Exception.EmailAlreadyExistsException;
import MyAcad.Project.backend.Exception.LegajoAlreadyExistsException;
import MyAcad.Project.backend.Mapper.TeacherMapper;
import MyAcad.Project.backend.Model.Programs.Program;
import MyAcad.Project.backend.Model.Programs.ProgramResponse;
import MyAcad.Project.backend.Model.Users.*;
import MyAcad.Project.backend.Repository.Programs.ProgramRepository;
import MyAcad.Project.backend.Repository.Users.TeacherRepository;
import MyAcad.Project.backend.Service.Programs.ProgramService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
public class TeacherService {
    private final TeacherRepository repository;
    private final ProgramService programService;
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

    public List<TeacherCsvDto> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<TeacherCsvDto> csvToBean = new CsvToBeanBuilder<TeacherCsvDto>(reader)
                    .withType(TeacherCsvDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        }
    }

    public void saveStudentByCsv(List<TeacherCsvDto> records){
        for (TeacherCsvDto record : records) {
            Teacher teacher = new Teacher();
            teacher.setEmail(record.getEmail());
            teacher.setPassword(String.valueOf(record.getDni()));
            teacher.setActive(true);
            teacher.setDni(Integer.parseInt((record.getDni())));
            teacher.setName(record.getName());
            teacher.setLastName(record.getLastname());
            teacher.setRole(Role.TEACHER);
            add(teacher);
        }
    }

    public Page<TeacherResponse> listTeachersPaginated(int page, int size) {
        Page<Teacher> teacherPage = repository.findAll(PageRequest.of(page, size));

        List<TeacherResponse> responseList = teacherPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .map(this::getProgramsForTeacher)   // ✔ ahora sí tiene sentido
                .toList();

        return new PageImpl<>(
                responseList,
                teacherPage.getPageable(),
                teacherPage.getTotalElements()
        );
    }

    public List<TeacherResponse> getByCommission(Long commissionId) {

        List<Long> teacherIds = repository.findTeachersByCommissionId(commissionId);

        return mapper.toResponseList(repository.findByIdIn(teacherIds))
                .stream()
                .map(this::getProgramsForTeacher)
                .toList();
    }


    public List<TeacherResponse> getByLegajoContaining(String legajo) {
        return mapper.toResponseList(repository.findByLegajoContaining(legajo))
                .stream()
                .map(this::getProgramsForTeacher)
                .toList();
    }

    public List<TeacherResponse> getByFullName(String fullName) {

        return mapper.toResponseList(repository.findByFullName(fullName))
                .stream().map(this::getProgramsForTeacher).toList();
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
        return mapper.toResponseList(repository.findAll())
                .stream()
                .map(this::getProgramsForTeacher)
                .toList();
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
        return repository.findById(id)
                .map(mapper::toResponse)
                .map(this::getProgramsForTeacher); 
    }

    public Optional<TeacherResponse> getByLegajo(String legajo) {
        return repository.findByLegajo(legajo)
                .map(mapper::toResponse)
                .map(this::getProgramsForTeacher);
    }

    public Optional<TeacherResponse> getByEmail(String email){
        return repository.findByEmail(email)
                .map(mapper::toResponse)
                .map(this::getProgramsForTeacher);
    }


    public TeacherResponse getProgramsForTeacher(TeacherResponse teacher) {
        List<ProgramResponse> programList = programService.findByTeacher(teacher.getId());

        teacher.setPrograms(programList);
        return teacher;
    }
}
