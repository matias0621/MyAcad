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
import MyAcad.Project.backend.Model.Academic.Commission;
import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import MyAcad.Project.backend.Repository.Academic.CommissionRepository;
import MyAcad.Project.backend.Repository.Academic.SubjectsRepository;
import MyAcad.Project.backend.Repository.Programs.ProgramRepository;
import MyAcad.Project.backend.Repository.Users.TeacherRepository;
import MyAcad.Project.backend.Service.Programs.ProgramService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TeacherService {
    private final TeacherRepository repository;
    private final ProgramRepository programRepository;
    private final ProgramService programService;
    private final TeacherMapper mapper;
    private final UserLookupService userLookupService;
    private final CommissionRepository commissionRepository;
    private final SubjectsRepository subjectsRepository;
    private final EntityManager entityManager;
    private PasswordEncoder passwordEncoder;

    public Teacher add(Teacher t) {
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

        return repository.save(t);
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

    public void saveTeacherByCsv(List<TeacherCsvDto> records) {

        for (TeacherCsvDto record: records){
            if(userLookupService.findByEmail(record.getEmail()).isPresent()) {
                throw new EmailAlreadyExistsException();
            }else if(userLookupService.findByDni(Integer.parseInt(record.getDni())).isPresent()) {
                throw new DniAlreadyExistsException();
            }
        }

        for (TeacherCsvDto record : records) {

            Teacher teacher = new Teacher();
            teacher.setEmail(record.getEmail());
            teacher.setPassword(record.getDni());
            teacher.setActive(true);
            teacher.setDni(Integer.parseInt(record.getDni()));
            teacher.setName(record.getName());
            teacher.setLastName(record.getLastname());
            teacher.setRole(Role.TEACHER);

            // 1) guardo primero el teacher (para generar ID)
            teacher = add(teacher);

            // 2) asigno la carrera (si existe)
            if (record.getCareer() != null && !record.getCareer().isBlank()) {

                String[] careers = record.getCareer().split(";");

                for (String careerName : careers) {
                    Teacher finalTeacher = teacher;
                    programRepository.findByName(careerName.trim())
                            .ifPresent(program -> {
                                program.getTeachers().add(finalTeacher);
                                programRepository.save(program);
                            });
                }
            }
        }
    }

    public Page<TeacherResponse> listTeachersPaginated(int page, int size) {
        Page<Teacher> teacherPage = repository.findAll(PageRequest.of(page, size));

        List<TeacherResponse> responseList = teacherPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .map(this::getProgramsForTeacher) 
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

    public TeacherResponse getByCommissionIdAndSubjectId(Long commissionId, Long subjectId) {
        return mapper.toResponse(repository.findTeacherByCommissionAndSubject(commissionId,subjectId).orElseThrow(() -> new RuntimeException("No existe el profe")));
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
        
        List<String> conflicts = new ArrayList<>();
        
        List<Commission> commissions = commissionRepository.findCommissionsByTeacherId(teacherId);
        if (commissions != null && !commissions.isEmpty()) {
            List<String> commissionNumbers = commissions.stream()
                    .filter(c -> c != null && c.getNumber() != 0 && c.getProgram() != null)
                    .map(c -> "Comisión " + c.getNumber() + " (" + c.getProgram() + ")")
                    .toList();
            if (!commissionNumbers.isEmpty()) {
                conflicts.add("Comisiones: " + String.join(", ", commissionNumbers));
            }
        }
        
        List<SubjectsEntity> subjects = subjectsRepository.findSubjectsByTeacherId(teacherId);
        if (subjects != null && !subjects.isEmpty()) {
            List<String> subjectNames = subjects.stream()
                    .filter(s -> s != null && s.getName() != null)
                    .map(SubjectsEntity::getName)
                    .toList();
            if (!subjectNames.isEmpty()) {
                conflicts.add("Materias: " + String.join(", ", subjectNames));
            }
        }
        
        List<Program> programs = programRepository.findByTeacher(teacherId);
        if (programs != null && !programs.isEmpty()) {
            List<String> programNames = programs.stream()
                    .filter(p -> p != null && p.getName() != null)
                    .map(Program::getName)
                    .toList();
            if (!programNames.isEmpty()) {
                conflicts.add("Programas: " + String.join(", ", programNames));
            }
        }
        
        if (!conflicts.isEmpty()) {
            String errorMsg = "No se puede eliminar el profesor porque está asignado en: " + String.join("; ", conflicts);
            throw new RuntimeException(errorMsg);
        }
        
        return deleteTeacherRelations(teacherId);
    }
    
    @Transactional
    protected ResponseEntity<Void> deleteTeacherRelations(Long teacherId) {
        try {
            try {
                entityManager.createNativeQuery("DELETE FROM teacher_x_commission WHERE user_id = :teacherId")
                        .setParameter("teacherId", teacherId)
                        .executeUpdate();
            } catch (Exception e) {
            }
            
            try {
                entityManager.createNativeQuery("DELETE FROM subject_x_teacher WHERE user_id = :teacherId")
                        .setParameter("teacherId", teacherId)
                        .executeUpdate();
            } catch (Exception e) {
            }
            
            try {
                entityManager.createNativeQuery("DELETE FROM program_teachers WHERE teachers_id = :teacherId")
                        .setParameter("teacherId", teacherId)
                        .executeUpdate();
            } catch (Exception e) {
                try {
                    entityManager.createNativeQuery("DELETE FROM program_teachers WHERE user_id = :teacherId")
                            .setParameter("teacherId", teacherId)
                            .executeUpdate();
                } catch (Exception e2) {
                }
            }
            
            entityManager.flush();
            repository.deleteById(teacherId);
            repository.flush();
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error al eliminar profesor " + teacherId + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error inesperado al eliminar el profesor: " + e.getMessage(), e);
        }
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
