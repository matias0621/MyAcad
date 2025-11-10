package MyAcad.Project.backend.Service.Academic;

import MyAcad.Project.backend.Enum.AcademicStatus;
import MyAcad.Project.backend.Exception.InscriptionException;
import MyAcad.Project.backend.Mapper.CommissionMapper;
import MyAcad.Project.backend.Model.Academic.*;
import MyAcad.Project.backend.Model.Programs.Career;
import MyAcad.Project.backend.Model.Programs.Course;
import MyAcad.Project.backend.Model.Programs.Program;
import MyAcad.Project.backend.Model.Programs.Technical;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Repository.Academic.CommissionRepository;
import MyAcad.Project.backend.Repository.Programs.CareerRepository;
import MyAcad.Project.backend.Repository.Programs.CourseRepository;
import MyAcad.Project.backend.Repository.Programs.TechnicalRepository;
import MyAcad.Project.backend.Repository.Users.StudentRepository;
import MyAcad.Project.backend.Repository.Academic.SubjectsRepository;
import MyAcad.Project.backend.Repository.Users.TeacherRepository;
import MyAcad.Project.backend.Service.SubjectsXStudentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.security.auth.Subject;
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
    private final CareerRepository careerRepository;
    private final TechnicalRepository technicalRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

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
                .map(commissionMapper::toResponse)
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

    public ResponseEntity<Void> definitiveDeleteCommission(Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public List<CommissionResponse> list() {
        return commissionMapper.toResponseList(repository.findAll());
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

        return commissionMapper.toResponseList(filteredList);
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

        return commissionMapper.toResponseList(filteredList);
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
        return commissionMapper.toResponseList(repository.findAllActives());
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
        return commissionMapper.toResponseList(repository.findByProgram(program));
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

        // Guardar según el tipo de programa
        switch (program) {
            case Career career -> careerRepository.save(career);
            case Technical technical -> technicalRepository.save(technical);
            case Course course -> courseRepository.save(course);
            default -> throw new RuntimeException("No existe ese programa");
        }

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
        Student s = studentRepository.findById(studentId).orElseThrow();
        Commission commission = repository.findById(commissionId).orElseThrow();
        SubjectsEntity subjectsEntity = subjectsRepository.findById(subjectsId).orElseThrow();
        try {
            registerToStudent(s, commission, subjectsEntity);
        }catch (InscriptionException e){
            throw new InscriptionException(e.getMessage());
        }
    }

    public void registerToStudent(Student student, Commission commission, SubjectsEntity subjectsEntity){

        Optional<Student> optStudent = studentRepository.findById(student.getId());

        if (optStudent.isEmpty()){
            throw new InscriptionException("El legajo no existe.");
        }

        if (commission.getStudents().size() >= commission.getCapacity()){
            throw new InscriptionException("Esta comision esta llena");
        }

        if (!commission.getStudents().contains(student)){
            commission.getStudents().add(student);
        }

        if (subjectsXStudentService.getSubjectsXStudentByStudentIdAndSubjectsId(student.getId(), subjectsEntity.getId()).isPresent()){
            throw new InscriptionException("El alumno ya está anotado a la materia.");
        }

        if (!subjectsEntity.getPrerequisites().isEmpty()){
            for (SubjectsEntity prerequisite : subjectsEntity.getPrerequisites()) {
                if (prerequisite.isSubjectActive()){
                    validatePrerequisite(student, prerequisite);
                }
            }
        }

        SubjectsXStudentDTO subjectsXStudentDTO = SubjectsXStudentDTO.builder()
                .subjectsId(subjectsEntity.getId())
                .studentId(student.getId())
                .academicStatus(AcademicStatus.INPROGRESS)
                .build();

        registerStudentToProgram(student, commission.getProgram());
        subjectsXStudentService.createSubjectsXStudent(subjectsXStudentDTO);


    }

    public void registerStudentToProgram(Student student, String programName){
        Program program = findProgramByName(programName);

        program.getStudents().add(student);
        switch (program) {
            case Career career -> careerRepository.save(career);
            case Technical technical -> technicalRepository.save(technical);
            case Course course -> courseRepository.save(course);
            default -> throw new RuntimeException("No existe esa materia");
        }
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
        switch (program) {
            case Career career -> careerRepository.save(career);
            case Technical technical -> technicalRepository.save(technical);
            case Course course -> courseRepository.save(course);
            default -> throw new RuntimeException("No existe esa materia");
        }
        teacherRepository.save(teacher);
        repository.save(commission);
    }

    private void validatePrerequisite(Student student, SubjectsEntity prerequisite) {
        Optional<SubjectsXStudentEntity> opt = subjectsXStudentService
                .getSubjectsXStudentByStudentIdAndSubjectsId(student.getId(), prerequisite.getId());

        if (opt.isEmpty()) {
            throw new RuntimeException("Subject not found");
        }

        SubjectsXStudentEntity sxStudentEntity = opt.get();
        AcademicStatus statusStudent = sxStudentEntity.getStateStudent();
        AcademicStatus statusRequired = prerequisite.getAcademicStatus();

        switch (statusRequired) {
            case COMPLETED -> {
                if (!(statusStudent.equals(AcademicStatus.COMPLETED) || statusStudent.equals(AcademicStatus.APPROVED))) {
                    throw new InscriptionException("El alumno no ha aprobado las correlativas.");
                }
            }
            case APPROVED -> {
                if (!statusStudent.equals(AcademicStatus.APPROVED)) {
                    throw new InscriptionException("El alumno no ha aprobado las correlativas.");
                }
            }
            default -> throw new InscriptionException("El alumno no ha aprobado las correlativas.");
        }
    }

    public List<Commission> getAllCommissionByStudent(Long studentId) {
        return repository.findCommissionsByStudentId(studentId);
    }

    public Program findProgramByName(String name) {
        return  careerRepository.findByName(name)
                .map(p -> (Program) p)
                .or(() -> courseRepository.findByName(name).map(p -> (Program) p))
                .or(() -> technicalRepository.findByName(name).map(p -> (Program) p))
                .orElseThrow(() -> new RuntimeException("Program not found"));
    }


    public List<CommissionResponse> findByTeacherId(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        List<Commission> commissions = teacher.getCommissions();
        return commissionMapper.toResponseList(commissions);
    }
}
