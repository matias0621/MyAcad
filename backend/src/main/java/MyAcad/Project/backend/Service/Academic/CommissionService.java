package MyAcad.Project.backend.Service.Academic;

import MyAcad.Project.backend.Enum.AcademicStatus;
import MyAcad.Project.backend.Exception.InscriptionException;
import MyAcad.Project.backend.Mapper.CommissionMapper;
import MyAcad.Project.backend.Model.Academic.*;
import MyAcad.Project.backend.Model.Programs.Program;
import MyAcad.Project.backend.Model.RegistrationStudent.RegisterStudentToCommissionByCsv;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.StudentCsvDto;
import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Repository.Academic.CommissionRepository;
import MyAcad.Project.backend.Repository.Programs.ProgramRepository;
import MyAcad.Project.backend.Repository.SubjectPrerequisiteRepository;
import MyAcad.Project.backend.Repository.SubjectsXStudentRepository;
import MyAcad.Project.backend.Repository.Users.StudentRepository;
import MyAcad.Project.backend.Repository.Academic.SubjectsRepository;
import MyAcad.Project.backend.Repository.Users.TeacherRepository;
import MyAcad.Project.backend.Service.SubjectPrerequisiteService;
import MyAcad.Project.backend.Service.SubjectsXStudentService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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
public class CommissionService {
    private final CommissionRepository repository;
    private final CommissionMapper commissionMapper;
    private final SubjectsRepository subjectsRepository;
    private final StudentRepository studentRepository;
    private final SubjectsXStudentService subjectsXStudentService;
    private final SubjectsXStudentRepository subjectsXStudentRepository;
    private final TeacherRepository teacherRepository;
    private final ProgramRepository programRepository;
    private final SubjectPrerequisiteRepository subjectPrerequisiteRepository;
    private final SubjectService subjectService;

    public void add(Commission c) {
        if (repository.findCommissionByNumberAndProgram(c.getNumber(), c.getProgram()).isPresent()) {
            throw new RuntimeException("Commission number already exists");
        }
        repository.save(c);
    }

    public Page<CommissionResponse> listCommissionPaginated(int page, int size) {
        Page<Commission> commissionPage = repository.findAll(PageRequest.of(page, size));
        List<CommissionResponse> responseList = commissionPage.getContent()
                .stream()
                .map(this::mapCommissionFull)
                .toList();

        return new PageImpl<>(
                responseList,
                commissionPage.getPageable(),
                commissionPage.getTotalElements()
        );
    }

    public ResponseEntity<Void> delete(Long id) {
        Optional<Commission> optionalCommission = repository.findById(id);
        if (optionalCommission.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Commission c = optionalCommission.get();
        c.setActive(false);
        repository.save(c);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<Void> definitiveDeleteCommission(Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        Optional<Commission> commissionOpt = repository.findById(id);
        if (commissionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Commission commission = commissionOpt.get();
        List<String> conflicts = new ArrayList<>();
        
        if (commission.getStudents() != null && !commission.getStudents().isEmpty()) {
            List<String> studentNames = commission.getStudents().stream()
                    .filter(s -> s != null && s.getName() != null && s.getLastName() != null)
                    .map(s -> s.getName() + " " + s.getLastName())
                    .toList();
            if (!studentNames.isEmpty()) {
                conflicts.add("Estudiantes: " + studentNames.size() + " estudiante(s) - " + String.join(", ", studentNames.stream().limit(5).toList()) + (studentNames.size() > 5 ? " y más..." : ""));
            }
        }
        
        if (commission.getTeachers() != null && !commission.getTeachers().isEmpty()) {
            List<String> teacherNames = commission.getTeachers().stream()
                    .filter(t -> t != null && t.getName() != null && t.getLastName() != null)
                    .map(t -> t.getName() + " " + t.getLastName())
                    .toList();
            if (!teacherNames.isEmpty()) {
                conflicts.add("Profesores: " + teacherNames.size() + " profesor(es) - " + String.join(", ", teacherNames.stream().limit(5).toList()) + (teacherNames.size() > 5 ? " y más..." : ""));
            }
        }
        
        if (commission.getSubjects() != null && !commission.getSubjects().isEmpty()) {
            List<String> subjectNames = commission.getSubjects().stream()
                    .filter(s -> s != null && s.getName() != null)
                    .map(SubjectsEntity::getName)
                    .toList();
            if (!subjectNames.isEmpty()) {
                conflicts.add("Materias: " + String.join(", ", subjectNames));
            }
        }
        
        List<SubjectsXStudentEntity> subjectsXStudent = subjectsXStudentRepository.findAll().stream()
                .filter(sxs -> sxs.getCommission() != null && sxs.getCommission().getId() != null && sxs.getCommission().getId().equals(id))
                .toList();
        if (!subjectsXStudent.isEmpty()) {
            conflicts.add("Inscripciones académicas: " + subjectsXStudent.size() + " inscripción(es)");
        }
        
        if (!conflicts.isEmpty()) {
            String errorMsg = "No se puede eliminar la comisión porque tiene: " + String.join("; ", conflicts);
            throw new RuntimeException(errorMsg);
        }
        
        try {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al eliminar la comisión: " + e.getMessage(), e);
        }
    }

    public List<RegisterStudentToCommissionByCsv> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<RegisterStudentToCommissionByCsv> csvToBean = new CsvToBeanBuilder<RegisterStudentToCommissionByCsv>(reader)
                    .withType(RegisterStudentToCommissionByCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        }
    }

    public void registerStudentByCsv(List<RegisterStudentToCommissionByCsv> listStudent, Long commissionId){
        Commission commission = repository.findById(commissionId).orElseThrow();

        for (RegisterStudentToCommissionByCsv studentToRegister: listStudent) {
            Student student = studentRepository.findByLegajo(studentToRegister.getLegajo()).orElseThrow();
            for (SubjectsEntity subjects: commission.getSubjects()) {
                registerToStudent(student, commission, subjects);
            }
        }

        repository.save(commission);
    }

    public List<CommissionResponse> list() {
        return repository.findAll().stream().map(this::mapCommissionFull).toList();
    }

    public List<CommissionResponse> listForStudentInfo(String programName, Long studentId) {
        List<Commission> list = repository.findStudentCommissionsByProgram(studentId, programName);

        // Creamos una nueva lista para almacenar copias filtradas
        List<Commission> filteredList = new ArrayList<>();

        for (Commission c : list) {
            // Filtrar materias en las que el estudiante está anotado
            List<SubjectsEntity> filteredSubjects = c.getSubjects().stream()
                    .filter(subj -> subjectsXStudentService.getSubjectsXStudentByStudentIdAndSubjectsId(studentId, subj.getId()).isPresent())
                    .toList();
            c.setSubjects(filteredSubjects);

            Commission filteredCommission = getCommission(c, filteredSubjects);

            filteredList.add(filteredCommission);
        }

        return filteredList.stream().map(this::mapCommissionFull).toList();
    }

    public List<CommissionResponse> listSubjectsNotEnrolled(String programName, Long studentId) {
        List<Commission> list = repository.findByProgram(programName); // <-- todas, no sólo las que estudia

        List<Commission> filteredList = new ArrayList<>();

        for (Commission c : list) {
            List<SubjectsEntity> filteredSubjects = c.getSubjects().stream()
                    .filter(subj -> {
                        // usar existsBy para eficiencia (implementalo en SubjectsXStudentRepository)
                        return subjectsXStudentService
                                .getSubjectsXStudentByStudentIdAndSubjectsId(studentId, subj.getId())
                                .isEmpty();
                    })
                    .toList();

            if (!filteredSubjects.isEmpty()) {
                Commission filteredCommission = getCommission(c, filteredSubjects);
                filteredList.add(filteredCommission);
            }
        }

        return filteredList.stream().map(this::mapCommissionFull).toList();
    }

    private static Commission getCommission(Commission c, List<SubjectsEntity> filteredSubjects) {
        Commission filteredCommission = new Commission();
        filteredCommission.setId(c.getId());
        filteredCommission.setNumber(c.getNumber());
        filteredCommission.setProgram(c.getProgram());
        filteredCommission.setCapacity(c.getCapacity());
        filteredCommission.setActive(c.isActive());
        filteredCommission.setSubjects(filteredSubjects);
        filteredCommission.setTeachers(c.getTeachers());
        filteredCommission.setStudents(List.of()); // opcional, para no mandar todos los alumnos
        return filteredCommission;
    }


    public CommissionResponse toResponse(Commission entity) {
        if (entity == null) return null;

        return CommissionResponse.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .program(entity.getProgram())
                .build();
    }

    public List<CommissionResponse> listActive() {
        return repository.findAllActives().stream().map(this::mapCommissionFull).toList();
    }

    public void modify(Long id, Commission c) {
        Optional<Commission> optionalOld = repository.findById(id);
        if (optionalOld.isEmpty()) {
            throw new RuntimeException("Commission not found");
        }
        Commission old = optionalOld.get();
        if (old.getNumber() != c.getNumber()) {
            if (repository.findCommissionByNumberAndProgram(c.getNumber(), c.getProgram()).isPresent()) {
                throw new RuntimeException("Commission number already exists");
            }
        }

        if (c.getCapacity() <= old.getStudents().size()){
            throw new RuntimeException("Capacity limit exceeded");
        }

        old.setNumber(c.getNumber());
        old.setSubjects(c.getSubjects());
        old.setStudents(c.getStudents());
        old.setProgram(c.getProgram());
        old.setCapacity(c.getCapacity());
        old.setActive(c.isActive());

        repository.save(old);
    }

    public Optional<CommissionResponse> getById(Long id){
        return repository.findById(id).map(commissionMapper::toResponse);
    }

    public List<CommissionResponse> findByProgram(String program) {

        return repository.findByProgram(program)
                .stream()
                .map(commission -> {

                    // 1️⃣ MapStruct solo lo básico
                    CommissionResponse response =
                            commissionMapper.toResponse(commission);

                    // 2️⃣ Subjects a mano
                    if (commission.getSubjects() != null) {
                        response.setSubjects(
                                commission.getSubjects()
                                        .stream()
                                        .map(subjectService::mapSubjectManually)
                                        .toList()
                        );
                    }

                    return response;
                })
                .toList();
    }


    public Optional<Optional<Commission>> getByNumber(int number) {
        return Optional.ofNullable(repository.findByNumber(number));
    }

    public void addSubjectToCommission(Long idCommission, Long idSubject) {
        Commission c = repository.findById(idCommission)
                .orElseThrow(() -> new RuntimeException("No se encontró la comisión con id: " + idCommission));


        Program program = findProgramByName(c.getProgram());

        SubjectsEntity s = subjectsRepository.findById(idSubject)
                .orElseThrow(() -> new RuntimeException("No se encontró la materia con id: " + idSubject));

        // Evitar duplicados en la comisión
        if (!c.getSubjects().contains(s)) {
            c.getSubjects().add(s);
        }

        // Evitar duplicados en el programa
        if (!program.getSubjects().contains(s)) {
            program.getSubjects().add(s);
        }

        programRepository.save(program);

        System.out.println("Comisión editada" + c);

        repository.save(c);
    }


    public void deleteSubjectsFromCommission(Long idCommission, Long idSubject){
        Commission c = repository.findById(idCommission).get();
        SubjectsEntity s = subjectsRepository.findById(idSubject).get();
        c.getSubjects().remove(s);
        repository.save(c);
    }

    public void registerStudentbyManager(String legajo, Long commissionId, Long subjectsId){
        Optional<Student> studentOptional = studentRepository.findByLegajo(legajo);
        Commission commission = repository.findById(commissionId).orElseThrow();
        SubjectsEntity subjectsEntity = subjectsRepository.findById(subjectsId).orElseThrow();

        if (studentOptional.isEmpty()){
            throw new InscriptionException("El legajo no existe.");
        }
        try {
            Student s = studentOptional.get();
            registerToStudent(s, commission, subjectsEntity);
        }catch (InscriptionException e){
            throw new InscriptionException(e.getMessage());
        }
    }

    public void registerStudentByToken(Long studentId, Long commissionId, Long subjectsId){
        Student s = studentRepository.findById(studentId).orElseThrow(() -> new InscriptionException(
                "No existe el estudiante con id " + studentId
        ));
        Commission commission = repository.findById(commissionId).orElseThrow(() -> new InscriptionException(
                "No existe la comisión con id " + commissionId
        ));
        SubjectsEntity subjectsEntity = subjectsRepository.findById(subjectsId).orElseThrow(() -> new InscriptionException(
                "No existe la materia con id " + subjectsId
        ));
        try {
            registerToStudent(s, commission, subjectsEntity);
        }catch (InscriptionException e){
            throw new InscriptionException(e.getMessage());
        }
    }

    @Transactional
    public void registerToStudent(
            Student student,
            Commission commission,
            SubjectsEntity subjectsEntity
    ) {

        Student s = studentRepository.findById(student.getId())
                .orElseThrow(() -> new InscriptionException("El estudiante no existe."));

        // 👤 Agregar estudiante a la comisión si no está
        if (!commission.getStudents().contains(s)) {
            commission.getStudents().add(s);
        }

        // ❌ Evitar doble inscripción a la misma materia
        if (subjectsXStudentService
                .getSubjectsXStudentByStudentIdAndSubjectsId(
                        s.getId(),
                        subjectsEntity.getId()
                )
                .isPresent()) {
            throw new InscriptionException("El estudiante ya está anotado a la materia.");
        }

        // 📚 Validar correlativas
        List<SubjectPrerequisiteEntity> prerequisites =
                subjectPrerequisiteRepository
                        .findBySubject_Id(subjectsEntity.getId());

        for (SubjectPrerequisiteEntity spr : prerequisites) {
            validatePrerequisite(
                    s,
                    spr.getPrerequisite(),
                    spr.getRequiredStatus()
            );
        }

        // 📝 Crear inscripción académica
        SubjectsXStudentDTO subjectsXStudentDTO =
                SubjectsXStudentDTO.builder()
                        .studentId(s.getId())
                        .subjectsId(subjectsEntity.getId())
                        .commissionId(commission.getId())
                        .academicStatus(AcademicStatus.INPROGRESS)
                        .build();

        registerStudentToProgram(s, commission.getProgram());
        subjectsXStudentService.createSubjectsXStudent(subjectsXStudentDTO);

        repository.save(commission);
    }


    public void registerStudentToProgram(Student student, String programName){
        Program program = findProgramByName(programName);

        program.getStudents().add(student);
        programRepository.save(program);
    }

    public void registerTeacherToProgram(String legajo, Long commissionId, Long subjectsId){
        Commission commission = repository.findById(commissionId).orElseThrow();
        SubjectsEntity subjectsEntity = subjectsRepository.findById(subjectsId).orElseThrow();
        Optional<Teacher> teacherOpt = teacherRepository.findByLegajo(legajo);

        if (teacherOpt.isEmpty()){
            throw new InscriptionException("El legajo no existe.");
        }
        Teacher teacher = teacherOpt.get();



        // Asignamos un profesor a la carrera
        Program program = findProgramByName(commission.getProgram());
        program.getTeachers().add(teacher);
        // Asignamos las comisiones y las materias a un profesor
        teacher.getCommissions().add(commission);
        teacher.getSubjects().add(subjectsEntity);
        // Si el teacher no esta en la comision lo agregamos
        if (!commission.getTeachers().contains(teacher)) commission.getTeachers().add(teacher);

        // Seteamos y guardamos la carrera
        programRepository.save(program);
        teacherRepository.save(teacher);
        repository.save(commission);
    }

    private void validatePrerequisite(
            Student student,
            SubjectsEntity prerequisite,
            AcademicStatus requiredStatus
    ) {

        SubjectsXStudentEntity sxStudentEntity =
                subjectsXStudentRepository
                        .findByStudent_IdAndSubjects_Id(
                                student.getId(),
                                prerequisite.getId()
                        )
                        .orElseThrow(() ->
                                new InscriptionException(
                                        "El alumno no cursó la materia correlativa: "
                                                + prerequisite.getName()
                                )
                        );

        AcademicStatus statusStudent = sxStudentEntity.getStateStudent();

        switch (requiredStatus) {

            case APPROVED -> {
                if (!(statusStudent == AcademicStatus.APPROVED
                        || statusStudent == AcademicStatus.COMPLETED)) {
                    throw new InscriptionException(
                            "La materia " + prerequisite.getName()
                                    + " debe estar APROBADA"
                    );
                }
            }

            case COMPLETED -> {
                if (statusStudent != AcademicStatus.COMPLETED) {
                    throw new InscriptionException(
                            "La materia " + prerequisite.getName()
                                    + " debe estar PROMOCIONADA"
                    );
                }
            }

            default -> throw new InscriptionException(
                    "Estado académico inválido para correlativas"
            );
        }
    }

    public List<Commission> getAllCommissionByStudent(Long studentId) {
        return repository.findCommissionsByStudentId(studentId);
    }

    public Program findProgramByName(String name) {
        return  programRepository.findByName(name).orElseThrow();
    }


    public List<CommissionResponse> findByTeacherId(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        List<Commission> commissions = teacher.getCommissions();
        return commissions.stream().map(this::mapCommissionFull).toList();
    }

    @Transactional
    public void unregister(Long studentId, Long subjectsId, Long commissionId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new InscriptionException("Estudiante inexistente"));

        SubjectsEntity subjectsEntity = subjectsRepository.findById(subjectsId)
                .orElseThrow(() -> new InscriptionException("Materia inexistente"));

        Commission commission = repository.findById(commissionId)
                .orElseThrow(() -> new InscriptionException("Comisión inexistente"));

        // 🔍 Buscar inscripción EXACTA
        SubjectsXStudentEntity inscription =
                subjectsXStudentRepository
                        .findByStudent_IdAndSubjects_IdAndCommission_Id(
                                studentId,
                                subjectsId,
                                commissionId
                        )
                        .orElseThrow(() ->
                                new InscriptionException(
                                        "El estudiante no está inscripto en esta materia y comisión"
                                )
                        );

        // 🗑️ Eliminar inscripción académica
        subjectsXStudentRepository.delete(inscription);

        // 👥 Remover de la comisión SOLO si no le queda ninguna materia en ella
        boolean stillInCommission =
                subjectsXStudentRepository
                        .existsByStudent_IdAndCommission_Id(studentId, commissionId);

        if (!stillInCommission) {
            commission.getStudents().remove(student);
            repository.save(commission);
        }
    }


    public void deleteSubjectXStudent(Long studentId, Long subjectsId) {
        SubjectsXStudentEntity subjectsXStudentEntity = subjectsXStudentRepository.findByStudent_IdAndSubjects_Id(studentId,subjectsId).orElseThrow(() -> new RuntimeException("SubjectsXStudent not found"));
        subjectsXStudentRepository.delete(subjectsXStudentEntity);
    }

    private CommissionResponse mapCommissionFull(Commission commission) {

        if (commission == null) return null;

        // 1️⃣ MapStruct: campos simples
        CommissionResponse response =
                commissionMapper.toResponse(commission);

        // 2️⃣ Subjects: MANUAL (correlativas controladas)
        if (commission.getSubjects() != null) {
            response.setSubjects(
                    commission.getSubjects()
                            .stream()
                            .map(subjectService::mapSubjectManually)
                            .toList()
            );
        }

        return response;
    }
}
