package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Model.FinalExam.FinalExamDTO;
import MyAcad.Project.backend.Model.FinalExam.FinalExamEntity;
import MyAcad.Project.backend.Model.Subjects.SubjectsEntity;
import MyAcad.Project.backend.Repository.FinalExamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinalExamService {
    private final FinalExamRepository finalExamRepository;
    private final SubjectService subjectService;

    public FinalExamEntity create(FinalExamDTO dto) {
        SubjectsEntity subject = subjectService.getById((long) dto.getSubjectId())
                .orElseThrow(() -> new EntityNotFoundException("Subject not found with id: " + dto.getSubjectId()));

        FinalExamEntity exam = FinalExamEntity.builder()
                .score(dto.getScore())
                .subject(subject)
                .build();

        return finalExamRepository.save(exam);
    }

    public List<FinalExamEntity> findAll() {
        return finalExamRepository.findAll();
    }

    public FinalExamEntity findById(Long id) {
        return finalExamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Final exam not found with id: " + id));
    }

    public List<FinalExamEntity> findAllBySubjectsId(Long id) {
        return finalExamRepository.findBySubject_Id(id);
    }

    public FinalExamEntity update(Long id, FinalExamDTO dto) {
        FinalExamEntity existingExam = finalExamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Final exam not found with id: " + id));

        SubjectsEntity subject = subjectService.getById((long) dto.getSubjectId())
                .orElseThrow(() -> new EntityNotFoundException("Subject not found with id: " + dto.getSubjectId()));

        existingExam.setScore(dto.getScore());
        existingExam.setSubject(subject);

        return finalExamRepository.save(existingExam);
    }

    public void delete(Long id) {
        if (!finalExamRepository.existsById(id)) {
            throw new EntityNotFoundException("Final exam not found with id: " + id);
        }
        finalExamRepository.deleteById(id);
    }

    public List<FinalExamEntity> findByScore(int score) {
        return finalExamRepository.findAll()
                .stream()
                .filter(e -> e.getScore() == score)
                .toList();
    }

}
