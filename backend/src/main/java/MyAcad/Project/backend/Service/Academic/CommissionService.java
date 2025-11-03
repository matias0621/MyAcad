package MyAcad.Project.backend.Service.Academic;

import MyAcad.Project.backend.Enum.AcademicStatus;
import MyAcad.Project.backend.Model.Academic.Commission;
import MyAcad.Project.backend.Model.MateriaXAlumno.SubjectsXStudentDTO;
import MyAcad.Project.backend.Model.Academic.SubjectsXStudentEntity;
import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Repository.Academic.CommissionRepository;
import MyAcad.Project.backend.Repository.Users.StudentRepository;
import MyAcad.Project.backend.Repository.Academic.SubjectsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.security.auth.Subject;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommissionService {
    private final CommissionRepository repository;
    private final SubjectsRepository subjectsRepository;
    private final StudentRepository studentRepository;
    private final SubjectsXStudentService subjectsXStudentService;

    public void add(Commission c) {
        if (repository.findCommissionByNumberAndProgram(c.getNumber(), c.getProgram()).isPresent()) {
            throw new RuntimeException("Commission number already exists");
        }
        repository.save(c);
    }

    public Page<Commission> listCommissionPaginated(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
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

    public List<Commission> list() {
        return repository.findAll();
    }
    public List<Commission> listActive() {
        return repository.findAllActives();
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

    public Optional<Commission> getById(Long id){
        return repository.findById(id);
    }

    public List<Commission> findByProgram(String program) {
        return repository.findByProgram(program);
    }

    public Optional<Optional<Commission>> getByNumber(int number) {
        return Optional.ofNullable(repository.findByNumber(number));
    }

    public void addSubjectsToCommission(Long idCommission, List<Long> idSubjects){
        Commission c = repository.findById(idCommission).get();
        for (Long idSubject : idSubjects) {
            SubjectsEntity s = subjectsRepository.findById(idSubject).get();
            if (!c.getSubjects().contains(s)) {
                c.getSubjects().add(s);
            }
        }
        repository.save(c);
    }

    public void deleteSubjectsFromCommission(Long idCommission, Long idSubject){
        Commission c = repository.findById(idCommission).get();
        SubjectsEntity s = subjectsRepository.findById(idSubject).get();
        c.getSubjects().remove(s);
        repository.save(c);
    }

    public void registerToStudent(Long studentId, Long commisionId, Long subjectsId){
        Commission commission = repository.findById(commisionId).get();
        Student student = studentRepository.findById(studentId).get();
        SubjectsEntity subjectsEntity = subjectsRepository.findById(subjectsId).get();

        if (!commission.getStudents().contains(student)){
            commission.getStudents().add(student);
        }

        if (subjectsXStudentService.getSubjectsXStudentByStudentIdAndSubjectsId(studentId, subjectsId).isPresent()){
            throw new RuntimeException("Subject already exists");
        }

        if (!subjectsEntity.getPrerequisites().isEmpty()){
            for (SubjectsEntity prerequisite : subjectsEntity.getPrerequisites()) {
                validatePrerequisite(student, prerequisite);
            }
        }

        SubjectsXStudentDTO subjectsXStudentDTO = SubjectsXStudentDTO.builder()
                .subjectsId(subjectsId)
                .studentId(studentId)
                .academicStatus(AcademicStatus.INPROGRESS)
                .build();

        subjectsXStudentService.createSubjectsXStudent(subjectsXStudentDTO);

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
                    throw new RuntimeException("Can't register in this subject");
                }
            }
            case APPROVED -> {
                if (!statusStudent.equals(AcademicStatus.APPROVED)) {
                    throw new RuntimeException("Can't register in this subject");
                }
            }
            default -> throw new RuntimeException("Can't register in this subject");
        }
    }

    public List<Commission> getAllCommissionByStudent(Long studentId) {
        return repository.findCommissionsByStudentId(studentId);
    }



}
