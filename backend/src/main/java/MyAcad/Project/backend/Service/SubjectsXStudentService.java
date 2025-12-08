package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Enum.AcademicStatus;
import MyAcad.Project.backend.Enum.ExamType;
import MyAcad.Project.backend.Model.Academic.ExamsEntity;
import MyAcad.Project.backend.Model.Academic.SubjectsXStudentDTO;
import MyAcad.Project.backend.Model.Academic.SubjectsXStudentEntity;
import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Repository.Academic.ExamsRepository;
import MyAcad.Project.backend.Repository.SubjectsXStudentRepository;
import MyAcad.Project.backend.Repository.Users.StudentRepository;
import MyAcad.Project.backend.Service.Academic.SubjectService;
import MyAcad.Project.backend.Service.Users.StudentService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.security.auth.Subject;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectsXStudentService {
    private final SubjectsXStudentRepository subjectsXStudentRepository;
    private final SubjectService subjectService;
    private final StudentRepository studentRepository;
    private final ExamsRepository examsRepository;

    public void createSubjectsXStudent(SubjectsXStudentDTO subjectsXStudentDTO) {
        SubjectsEntity subjects = subjectService.getById(subjectsXStudentDTO.getSubjectsId()).orElseThrow();
        Student student = studentRepository.findById(subjectsXStudentDTO.getStudentId()).orElseThrow();

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
                .build();

        subjectsXStudentRepository.save(subjectsXStudentEntity);
    }

    public List<SubjectsXStudentEntity> getAllSubjectsXStudent() {
        return subjectsXStudentRepository.findAll();
    }

    public SubjectsXStudentEntity getSubjectsXStudentById(Long id) {
        return subjectsXStudentRepository.findById(id).orElseThrow();
    }

    public List<SubjectsXStudentEntity> getAllSubjectsXStudentByStudentId(Long studentId) {
        return subjectsXStudentRepository.findByStudent_Id(studentId);
    }

    public Optional<SubjectsXStudentEntity> getSubjectsXStudentByStudentIdAndSubjectsId(Long studentId, Long subjectsId) {
        return subjectsXStudentRepository.findByStudent_IdAndSubjects_Id(studentId, subjectsId);
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

    public void updateAcademicStatusForExams(Student student, SubjectsEntity subject) {
        List<ExamsEntity> examsEntities = examsRepository.findAllBySubject_IdAndStudent_Id(subject.getId() ,student.getId());
        SubjectsXStudentEntity subjectsXStudentEntity = subjectsXStudentRepository.findByStudent_IdAndSubjects_Id(student.getId(), subject.getId()).orElseThrow();
        List<ExamsEntity> exams = examsEntities
                .stream()
                .filter(e -> e.getExamType() == ExamType.EXAM)
                .toList();
        List<ExamsEntity> makeUpExams = examsEntities
                .stream()
                .filter(e -> e.getExamType() == ExamType.MAKEUP_EXAM)
                .toList();
        List<ExamsEntity> finalExams = examsEntities
                .stream()
                .filter(e -> e.getExamType() == ExamType.FINAL_EXAM)
                .toList();

        if (!finalExams.isEmpty()){
            ExamsEntity finalExam = finalExams.getLast();
            if (finalExam.getScore() >= 60){
                subjectsXStudentEntity.setStateStudent(AcademicStatus.APPROVED);
                subjectsXStudentRepository.save(subjectsXStudentEntity);
            }
        }
        else if (makeUpExams.isEmpty() && !exams.isEmpty()) {
            boolean promotion = exams.stream().allMatch(n -> n.getScore() >= 80);
            boolean aprove = exams.stream().allMatch(n -> n.getScore() >= 60);

            if (promotion){
                subjectsXStudentEntity.setStateStudent(AcademicStatus.APPROVED);
                subjectsXStudentRepository.save(subjectsXStudentEntity);
            }
            else if (aprove){
                subjectsXStudentEntity.setStateStudent(AcademicStatus.COMPLETED);
                subjectsXStudentRepository.save(subjectsXStudentEntity);
            }
            else {
                subjectsXStudentEntity.setStateStudent(AcademicStatus.FAILED);
                subjectsXStudentRepository.save(subjectsXStudentEntity);
            }
        }
        else if (makeUpExams.size() == 1 && exams.size() == 1) {
            ExamsEntity exam = examsEntities.getFirst();
            ExamsEntity makeUpExam = makeUpExams.getFirst();

            if (exam.getScore() >= 80 && makeUpExam.getScore() >= 80) {
                subjectsXStudentEntity.setStateStudent(AcademicStatus.APPROVED);
                subjectsXStudentRepository.save(subjectsXStudentEntity);
            } else if (exam.getScore() >= 60 && makeUpExam.getScore() >= 60) {
                subjectsXStudentEntity.setStateStudent(AcademicStatus.COMPLETED);
                subjectsXStudentRepository.save(subjectsXStudentEntity);
            }
            else {
                subjectsXStudentEntity.setStateStudent(AcademicStatus.FAILED);
                subjectsXStudentRepository.save(subjectsXStudentEntity);
            }
        }
        else if (makeUpExams.size() == 1 && exams.size() >= 2){
            List<ExamsEntity> aproveExams = exams.stream().filter(e -> e.getScore() >= 60).toList();
            boolean promotion = aproveExams.stream().allMatch(n -> n.getScore() >= 80);
            boolean aprove = aproveExams.stream().allMatch(n -> n.getScore() >= 60);

            ExamsEntity makeUp = makeUpExams.getFirst();
            if (makeUp.getScore() >= 80 && promotion){
                subjectsXStudentEntity.setStateStudent(AcademicStatus.APPROVED);
                subjectsXStudentRepository.save(subjectsXStudentEntity);
            }
            else if (makeUp.getScore() >= 60 && aprove){
                subjectsXStudentEntity.setStateStudent(AcademicStatus.COMPLETED);
                subjectsXStudentRepository.save(subjectsXStudentEntity);
            }
            else {
                subjectsXStudentEntity.setStateStudent(AcademicStatus.FAILED);
                subjectsXStudentRepository.save(subjectsXStudentEntity);
            }
        }
        else if (makeUpExams.size() == exams.size()){
            boolean aprove = makeUpExams.stream().allMatch(n -> n.getScore() >= 60);

            if (aprove){
                subjectsXStudentEntity.setStateStudent(AcademicStatus.COMPLETED);
                subjectsXStudentRepository.save(subjectsXStudentEntity);
            }
            else {
                subjectsXStudentEntity.setStateStudent(AcademicStatus.FAILED);
                subjectsXStudentRepository.save(subjectsXStudentEntity);
            }
        }
    }

    public void deleteSubjectsXStudent(Long SubjectXStudentId) {
        SubjectsXStudentEntity subjectsXStudentEntity = subjectsXStudentRepository.findById(SubjectXStudentId).orElseThrow();
        subjectsXStudentRepository.delete(subjectsXStudentEntity);
    }
}
