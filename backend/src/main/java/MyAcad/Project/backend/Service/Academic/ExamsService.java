package MyAcad.Project.backend.Service.Academic;

import MyAcad.Project.backend.Mapper.ExamsMapper;
import MyAcad.Project.backend.Model.Academic.ExamsDTO;
import MyAcad.Project.backend.Model.Academic.ExamsEntity;
import MyAcad.Project.backend.Model.Academic.ExamsResponse;
import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Model.Users.TeacherResponse;
import MyAcad.Project.backend.Repository.Academic.ExamsRepository;
import MyAcad.Project.backend.Repository.Users.StudentRepository;
import MyAcad.Project.backend.Service.SubjectsXStudentService;
import MyAcad.Project.backend.Service.Users.StudentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamsService {
    private final ExamsRepository examsRepository;
    private final ExamsMapper examsMapper;
    private final SubjectService subjectService;
    private final StudentRepository studentRepository;
    private final SubjectsXStudentService subjectsXStudentService;

    public void create(ExamsDTO dto) {
        SubjectsEntity subject = subjectService.getById(dto.getSubjectId())
                .orElseThrow(() -> new EntityNotFoundException("Subject not found with id: " + dto.getSubjectId()));

        Student student = studentRepository.findByLegajo(dto.getLegajoStudent()).orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + dto.getLegajoStudent()));


        ExamsEntity exam = ExamsEntity.builder()
                .score(dto.getScore())
                .subject(subject)
                .student(student)
                .examType(dto.getExamType())
                .build();

        examsRepository.save(exam);
        subjectsXStudentService.updateAcademicStatusForExams(student, subject);
    }

    public Page<ExamsResponse> listExamsPaginated(int page, int size) {
        Page<ExamsEntity> examsPage = examsRepository.findAll(PageRequest.of(page, size));
        List<ExamsResponse> responseList = examsPage.getContent()
                .stream()
                .map(examsMapper::toExamsResponse)
                .toList();

        return new PageImpl<>(
                responseList,
                examsPage.getPageable(),
                examsPage.getTotalElements()
        );
    }

    public List<ExamsResponse> findAll() {
        return examsMapper.toExamsResponseList(examsRepository.findAll());
    }

    public ExamsResponse findById(Long id) {
        return examsRepository.findById(id).map(examsMapper::toExamsResponse).orElse(null);
    }

    public List<ExamsResponse> findByStudentId(Long studentId) {
        return examsMapper.toExamsResponseList(examsRepository.findAllByStudent_Id(studentId));
    }

    public List<ExamsResponse> findAllBySubjectsId(Long id) {
        return examsMapper.toExamsResponseList(examsRepository.findBySubject_Id(id));
    }

    public void update(Long id, ExamsDTO dto) {
        ExamsEntity existingExam = examsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Final exam not found with id: " + id));

        SubjectsEntity subject = subjectService.getById((long) dto.getSubjectId())
                .orElseThrow(() -> new EntityNotFoundException("Subject not found with id: " + dto.getSubjectId()));

        Student student = studentRepository.findByLegajo(dto.getLegajoStudent()).orElseThrow();

        existingExam.setScore(dto.getScore());
        existingExam.setSubject(subject);
        existingExam.setExamType(dto.getExamType());
        existingExam.setStudent(student);

        examsRepository.save(existingExam);
    }

    public void delete(Long id) {
        if (!examsRepository.existsById(id)) {
            throw new EntityNotFoundException("Final exam not found with id: " + id);
        }
        examsRepository.deleteById(id);
    }

    public ResponseEntity<Void> definitiveDeleteFinalExam(Long id) {
        if (!examsRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        examsRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public List<ExamsResponse> findByScore(int score) {
        return examsMapper.toExamsResponseList(examsRepository.findAll()
                .stream()
                .filter(e -> e.getScore() == score)
                .toList());
    }

}
