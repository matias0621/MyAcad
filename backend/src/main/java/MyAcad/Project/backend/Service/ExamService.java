package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Model.Exam.ExamDTO;
import MyAcad.Project.backend.Model.Exam.ExamEntity;
import MyAcad.Project.backend.Model.Subjects.SubjectsEntity;
import MyAcad.Project.backend.Repository.ExamRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;
    private final SubjectService subjectService;

    public void createExam(ExamDTO examDTO){

        SubjectsEntity subjects = subjectService.getById((long) examDTO.getSubjectId()).orElseThrow();

        ExamEntity examEntity = ExamEntity.builder()
                .score(examDTO.getScore())
                .subject(subjects)
                .build();

        examRepository.save(examEntity);
    }

    public ExamEntity updateExam(ExamDTO examDTO, Long id){
        ExamEntity examToUpdate = examRepository.findById(id).orElseThrow();
        examToUpdate.setScore(examDTO.getScore());
        examToUpdate.setSubject(subjectService.getById((long) examDTO.getSubjectId()).orElseThrow());
        return examRepository.save(examToUpdate);
    }

    public ResponseEntity<Void> deleteExam(Long id){

        if (!examRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        ExamEntity examToDelete = examRepository.findById(id).orElseThrow();
        examRepository.delete(examToDelete);
        return ResponseEntity.noContent().build();
    }

    public ExamEntity getExamById(Long id){
        return examRepository.findById(id).orElseThrow();
    }

    public List<ExamEntity> getAllExams(){
        return examRepository.findAll();
    }

    public List<ExamEntity> getAllExamsBySubjectsId(Long subjectId){
        return examRepository.findBySubject_Id(subjectId);
    }

    public List<ExamEntity> getAllExamsBySubjectsEntity(SubjectsEntity subjectEntity){
        return examRepository.findBySubject(subjectEntity);
    }


}
