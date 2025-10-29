package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Model.InscriptionToFinalExam.InscriptionToFinalExamDTO;
import MyAcad.Project.backend.Model.InscriptionToFinalExam.InscriptionToFinalExamEntity;
import MyAcad.Project.backend.Model.Subjects.SubjectsEntity;
import MyAcad.Project.backend.Repository.InscriptionToFinalExamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class InscriptionToFinalExamService {

    private final InscriptionToFinalExamRepository inscriptionToFinalExamRepository;
    private final SubjectService subjectService;

    public boolean createInscription(InscriptionToFinalExamDTO inscriptionToFinalExamDTO) {
        SubjectsEntity subjects = subjectService.getById(inscriptionToFinalExamDTO.getSubjectId()).orElseThrow();

        InscriptionToFinalExamEntity inscriptionToFinalExamEntity = InscriptionToFinalExamEntity.builder()
                .finalExamDate(inscriptionToFinalExamDTO.getFinalExamDate())
                .inscriptionDate(inscriptionToFinalExamDTO.getInscriptionDate())
                .subjects(subjects)
                .build();

        inscriptionToFinalExamRepository.save(inscriptionToFinalExamEntity);

        return true;
    }

    public List<InscriptionToFinalExamEntity> getAllInscriptions() {
        return inscriptionToFinalExamRepository.findAll();
    }

    public List<InscriptionToFinalExamEntity> getAllInscriptionsBySubjectId(Long subjectId) {
        SubjectsEntity subjects = subjectService.getById(subjectId).orElseThrow();

        return inscriptionToFinalExamRepository.findInscriptionToFinalExamEntitiesBySubjects(subjects);
    }

    public List<InscriptionToFinalExamEntity> getAllInscriptionsByInscriptionDate(String inscriptionDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime inscription = LocalDateTime.parse(inscriptionDate, formatter);
        return  inscriptionToFinalExamRepository.findInscriptionToFinalExamEntitiesByInscriptionDate(inscription);
    }

    public List<InscriptionToFinalExamEntity> getAllInscriptionsByExamDate(String examDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        LocalDateTime exam = LocalDateTime.parse(examDate, formatter);

        return inscriptionToFinalExamRepository.findInscriptionToFinalExamEntitiesByFinalExamDate(exam);
    }

    public InscriptionToFinalExamEntity updateInscription(InscriptionToFinalExamDTO inscriptionToFinalExamDTO, Long id) {
        InscriptionToFinalExamEntity inscriptionToFinalExamEntity = inscriptionToFinalExamRepository.findById(id).orElseThrow();
        inscriptionToFinalExamEntity.setFinalExamDate(inscriptionToFinalExamDTO.getFinalExamDate());
        inscriptionToFinalExamEntity.setInscriptionDate(inscriptionToFinalExamDTO.getInscriptionDate());
        inscriptionToFinalExamEntity.setSubjects(subjectService.getById(inscriptionToFinalExamDTO.getSubjectId()).orElseThrow());

        return inscriptionToFinalExamRepository.save(inscriptionToFinalExamEntity);

    }

    public void deleteInscription(Long id) {
        inscriptionToFinalExamRepository.deleteById(id);
    }

}
