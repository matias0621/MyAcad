package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Mapper.SubjectsXStudentMapper;
import MyAcad.Project.backend.Model.Academic.*;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.StudentResponse;
import MyAcad.Project.backend.Repository.Academic.CommissionRepository;
import MyAcad.Project.backend.Repository.Academic.ExamsRepository;
import MyAcad.Project.backend.Repository.SubjectsXStudentRepository;
import MyAcad.Project.backend.Repository.Users.StudentRepository;
import MyAcad.Project.backend.Service.Academic.SubjectService;
import MyAcad.Project.backend.Service.Users.StudentService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectsXStudentService {
    private final SubjectsXStudentRepository subjectsXStudentRepository;
    private final SubjectsXStudentMapper subjectsXStudentMapper;
    private final SubjectService subjectService;
    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final ExamsRepository examsRepository;
    private final CommissionRepository commissionRepository;

    public void createSubjectsXStudent(SubjectsXStudentDTO subjectsXStudentDTO) {
        SubjectsEntity subjects = subjectService.getById(subjectsXStudentDTO.getSubjectsId()).orElseThrow();
        Student student = studentRepository.findById(subjectsXStudentDTO.getStudentId()).orElseThrow();
        Commission commission = commissionRepository.findById(subjectsXStudentDTO.getCommissionId()).orElseThrow();

        // Verificar si ya está inscripto
        Optional<SubjectsXStudentEntity> existingRelation =
                subjectsXStudentRepository.findByStudent_IdAndSubjects_Id(student.getId(), subjects.getId());

        if (existingRelation.isPresent()) {
            throw new RuntimeException("El alumno ya está anotado en esta materia");
        }

        SubjectsXStudentEntity subjectsXStudentEntity = SubjectsXStudentEntity.builder()
                .student(student)
                .subjects(subjects)
                .stateStudent(subjectsXStudentDTO.getAcademicStatus())
                .commission(commission)
                .build();

        subjectsXStudentRepository.save(subjectsXStudentEntity);
    }

    public List<SubjectsXStudentResponse> getAllSubjectsXStudent() {
        return subjectsXStudentMapper.toResponseList(subjectsXStudentRepository.findAll());
    }

    public SubjectsXStudentResponse getSubjectsXStudentById(Long id) {
        return subjectsXStudentMapper.toResponse(subjectsXStudentRepository.findById(id).orElseThrow());
    }

    public List<SubjectsXStudentResponse> getAllSubjectsXStudentByStudentId(Long studentId) {
        return subjectsXStudentMapper.toResponseList(subjectsXStudentRepository.findByStudent_Id(studentId));
    }

    public Optional<SubjectsXStudentResponse> getSubjectsXStudentByStudentIdAndSubjectsId(Long studentId, Long subjectsId) {
        return subjectsXStudentRepository
                .findByStudent_IdAndSubjects_Id(studentId, subjectsId)
                .map(subjectsXStudentMapper::toResponse);
    }

    public Optional<SubjectsXStudentResponse> getSubjectsXStudentByStudentIdSubjectsIdAndCommissionId(Long studentId, Long commissionId, Long subjectsId) {
        return subjectsXStudentRepository.findByStudent_IdAndSubjects_IdAndCommission_Id(studentId,subjectsId,commissionId).map(subjectsXStudentMapper::toResponse);
    }

    public void updateSubjectsXStudent(SubjectsXStudentDTO subjectsXStudentDTO, Long SubjectXStudentId) {
        SubjectsXStudentEntity subjectsXStudentEntity = subjectsXStudentRepository.findById(SubjectXStudentId).orElseThrow();
        SubjectsEntity subjects = subjectService.getById(subjectsXStudentDTO.getSubjectsId()).orElseThrow();
        Student student = studentRepository.findById(subjectsXStudentDTO.getStudentId()).orElseThrow();

        subjectsXStudentEntity.setStudent(student);
        subjectsXStudentEntity.setSubjects(subjects);
        subjectsXStudentEntity.setStateStudent(subjectsXStudentDTO.getAcademicStatus());
        subjectsXStudentRepository.save(subjectsXStudentEntity);

    }

    public void deleteSubjectsXStudent(Long SubjectXStudentId) {
        SubjectsXStudentEntity subjectsXStudentEntity = subjectsXStudentRepository.findById(SubjectXStudentId).orElseThrow();
        subjectsXStudentRepository.delete(subjectsXStudentEntity);
    }
}
