package MyAcad.Project.backend.Service.Inscriptions;

import MyAcad.Project.backend.Mapper.InscriptionToFinalExamMapper;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToFinalExam.InscriptionToFinalExamDTO;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToFinalExam.InscriptionToFinalExamEntity;
import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToFinalExam.InscriptionToFinalExamResponse;
import MyAcad.Project.backend.Model.Programs.Program;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Repository.Inscriptions.InscriptionToFinalExamRepository;
import MyAcad.Project.backend.Repository.Programs.ProgramRepository;
import MyAcad.Project.backend.Repository.Users.StudentRepository;
import MyAcad.Project.backend.Service.Academic.SubjectService;
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
    private final StudentRepository studentRepository;
    private final ProgramRepository programRepository;
    private final InscriptionToFinalExamMapper inscriptionToFinalExamMapper;

    public void createInscription(InscriptionToFinalExamDTO inscriptionToFinalExamDTO) {
        SubjectsEntity subjects = subjectService.getById(inscriptionToFinalExamDTO.getSubjectsId()).orElseThrow();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime examDate = LocalDateTime.parse(inscriptionToFinalExamDTO.getFinalExamDate(), formatter);
        LocalDateTime inscriptionDate = LocalDateTime.parse(inscriptionToFinalExamDTO.getInscriptionDate(), formatter);
        Program program = programRepository.findByName(inscriptionToFinalExamDTO.getProgram()).orElseThrow();

        if (!examDate.isAfter(inscriptionDate)) {
            throw new IllegalArgumentException("La fecha del examen debe ser posterior a la fecha de inscripción.");
        }

        InscriptionToFinalExamEntity inscriptionToFinalExamEntity = InscriptionToFinalExamEntity.builder()
                .finalExamDate(examDate)
                .inscriptionDate(inscriptionDate)
                .subjects(subjects)
                .program(program)
                .build();

        inscriptionToFinalExamRepository.save(inscriptionToFinalExamEntity);
    }

    public List<InscriptionToFinalExamResponse> getAllInscriptions() {
        return inscriptionToFinalExamMapper.toResponseList(inscriptionToFinalExamRepository.findAll());
    }

    public InscriptionToFinalExamResponse getInscriptionById(Long id) {
        return inscriptionToFinalExamMapper.toResponse(inscriptionToFinalExamRepository.findById(id).orElseThrow());
    }

    public List<InscriptionToFinalExamResponse> getAllInscriptionsBySubjectId(Long subjectId) {
        SubjectsEntity subjects = subjectService.getById(subjectId).orElseThrow();

        return inscriptionToFinalExamMapper.toResponseList(inscriptionToFinalExamRepository.findInscriptionToFinalExamEntitiesBySubjects(subjects));
    }

    public List<InscriptionToFinalExamResponse> getAllInscriptionsByInscriptionDate(String inscriptionDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime inscription = LocalDateTime.parse(inscriptionDate, formatter);
        return  inscriptionToFinalExamMapper.toResponseList(inscriptionToFinalExamRepository.findInscriptionToFinalExamEntitiesByInscriptionDate(inscription));
    }

    public List<InscriptionToFinalExamResponse> getAllInscriptionsByExamDate(String examDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        LocalDateTime exam = LocalDateTime.parse(examDate, formatter);

        return inscriptionToFinalExamMapper.toResponseList(inscriptionToFinalExamRepository.findInscriptionToFinalExamEntitiesByFinalExamDate(exam));
    }

    public void updateInscription(InscriptionToFinalExamDTO inscriptionToFinalExamDTO, Long id) {
        InscriptionToFinalExamEntity inscriptionToFinalExamEntity = inscriptionToFinalExamRepository.findById(id).orElseThrow();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime examDate = LocalDateTime.parse(inscriptionToFinalExamDTO.getFinalExamDate(), formatter);
        LocalDateTime inscriptionDate = LocalDateTime.parse(inscriptionToFinalExamDTO.getInscriptionDate(), formatter);

        if (!examDate.isAfter(inscriptionDate)) {
            throw new IllegalArgumentException("La fecha del examen debe ser posterior a la fecha de inscripción.");
        }

        inscriptionToFinalExamEntity.setFinalExamDate(examDate);
        inscriptionToFinalExamEntity.setInscriptionDate(inscriptionDate);
        inscriptionToFinalExamEntity.setSubjects(subjectService.getById(inscriptionToFinalExamDTO.getSubjectsId()).orElseThrow());

        inscriptionToFinalExamRepository.save(inscriptionToFinalExamEntity);

    }

    public void addToStudent(Long inscriptionId, Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        InscriptionToFinalExamEntity inscription = inscriptionToFinalExamRepository.findById(inscriptionId).orElseThrow();

        if (inscription.getStudents().contains(student)){
            throw new RuntimeException("Ya estas registrado al examen");
        }
        inscription.getStudents().add(student);
        inscriptionToFinalExamRepository.save(inscription);
    }

    public void unregisterStudent(Long inscriptionId, Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        InscriptionToFinalExamEntity inscription = inscriptionToFinalExamRepository.findById(inscriptionId).orElseThrow();

        inscription.getStudents().remove(student);
        inscriptionToFinalExamRepository.save(inscription);
    }

    public List<InscriptionToFinalExamResponse> getActiveInscriptionsForStudent(Long studentId) {
        LocalDateTime now = LocalDateTime.now();
        return inscriptionToFinalExamMapper.toResponseList(inscriptionToFinalExamRepository.findActiveInscriptionsForStudent(studentId, now));
    }

    public void deleteInscription(Long id) {
        inscriptionToFinalExamRepository.deleteById(id);
    }

}
