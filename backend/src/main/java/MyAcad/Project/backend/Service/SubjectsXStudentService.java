package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Model.MateriaXAlumno.SubjectsXStudentDTO;
import MyAcad.Project.backend.Model.MateriaXAlumno.SubjectsXStudentEntity;
import MyAcad.Project.backend.Model.Subjects.SubjectsEntity;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Repository.SubjectsXStudentRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectsXStudentService {
    private final SubjectsXStudentRepository subjectsXStudentRepository;
    private final SubjectService subjectService;
    private final StudentService studentService;

    public void createSubjectsXStudent(SubjectsXStudentDTO subjectsXStudentDTO) {
        SubjectsEntity subjects = subjectService.getById(subjectsXStudentDTO.getSubjectsId()).orElseThrow();
        Student student = studentService.getById(subjectsXStudentDTO.getStudentId()).orElseThrow();

        SubjectsXStudentEntity subjectsXStudentEntity = SubjectsXStudentEntity.builder()
                .student(student)
                .subjects(subjects)
                .stateStudent(subjectsXStudentDTO.getAcademicStatus())
                .build();

        subjectsXStudentRepository.save(subjectsXStudentEntity);
    }

    public List<SubjectsXStudentEntity> getAllSubjectsXStudent() {
        return subjectsXStudentRepository.findAll();
    }

    public List<SubjectsXStudentEntity> getAllSubjectsXStudentByStudentId(Long studentId) {
        return subjectsXStudentRepository.findByStudent_Id(studentId);
    }

    public SubjectsXStudentEntity getSubjectsXStudentByStudentIdAndSubjectsId(Long studentId, Long subjectsId) {
        return subjectsXStudentRepository.findByStudent_IdAndSubjects_Id(studentId, subjectsId).orElseThrow();
    }

    public void updateSubjectsXStudent(SubjectsXStudentDTO subjectsXStudentDTO, Long SubjectXStudentId) {
        SubjectsXStudentEntity subjectsXStudentEntity = subjectsXStudentRepository.findById(SubjectXStudentId).orElseThrow();
        SubjectsEntity subjects = subjectService.getById(subjectsXStudentDTO.getSubjectsId()).orElseThrow();
        Student student = studentService.getById(subjectsXStudentDTO.getStudentId()).orElseThrow();

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
