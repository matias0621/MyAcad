package MyAcad.Project.backend.Service.Inscriptions;

import MyAcad.Project.backend.Mapper.InscriptionToCommissionMapper;
import MyAcad.Project.backend.Model.Academic.Commission;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission.InscriptionToCommissionDTO;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission.InscriptionToCommissionEntity;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission.InscriptionToCommissionResponse;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToFinalExam.InscriptionToFinalExamDTO;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToFinalExam.InscriptionToFinalExamEntity;
import MyAcad.Project.backend.Repository.Academic.CommissionRepository;
import MyAcad.Project.backend.Repository.Inscriptions.InscriptionToCommissionRepository;
import MyAcad.Project.backend.Service.Academic.CommissionService;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
public class InscriptionToCommissionService {
    private final InscriptionToCommissionRepository inscriptionToCommissionRepository;
    private final CommissionRepository commissionRepository;
    private final InscriptionToCommissionMapper inscriptionToCommissionMapper;

    public void createInscription(InscriptionToCommissionDTO dto){
        Commission commission = commissionRepository.findById(dto.getCommissionId()).orElseThrow();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        LocalDateTime inscriptionDate = LocalDateTime.parse(dto.getInscriptionDate(), formatter);
        LocalDateTime finishInscriptionDate = LocalDateTime.parse(dto.getFinishInscriptionDate(), formatter);

        if (inscriptionDate.isAfter(finishInscriptionDate)) {
            throw new RuntimeException("Inscription date is after finish inscription date");
        }

        InscriptionToCommissionEntity inscription = InscriptionToCommissionEntity.builder()
                .commission(commission)
                .finishInscriptionDate(inscriptionDate)
                .inscriptionDate(inscriptionDate)
                .build();

        inscriptionToCommissionRepository.save(inscription);
    }

    public List<InscriptionToCommissionResponse> findAll(){
        return inscriptionToCommissionMapper.toResponseList(inscriptionToCommissionRepository.findAll());
    }

    public List<InscriptionToCommissionResponse> findAllByCommissionId(Long commissionId){
        Commission commission = commissionRepository.findById(commissionId).orElseThrow();

        return inscriptionToCommissionMapper.toResponseList(inscriptionToCommissionRepository.findByCommission(commission));
    }

    public InscriptionToCommissionResponse findById(Long id){
        return inscriptionToCommissionMapper.toResponse(inscriptionToCommissionRepository.findById(id).orElseThrow());
    }

    public List<InscriptionToCommissionResponse> findByinscriptionDate(String inscriptionDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(inscriptionDate, formatter);

        return inscriptionToCommissionMapper.toResponseList(inscriptionToCommissionRepository.findInscriptionToFinalExamEntitiesByInscriptionDate(localDateTime));
    }

    public List<InscriptionToCommissionResponse> findByfinishInscription(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);

        return inscriptionToCommissionMapper.toResponseList(inscriptionToCommissionRepository.findInscriptionToFinalExamEntitiesByFinishInscriptionDate(localDateTime));
    }

    public List<InscriptionToCommissionResponse> findByInscriptionDateBeforeAndFinishInscriptionDateAfter(){
        return inscriptionToCommissionMapper.toResponseList(inscriptionToCommissionRepository.findByInscriptionDateBeforeAndFinishInscriptionDateAfter(LocalDateTime.now(), LocalDateTime.now()));
    }

    public List<InscriptionToCommissionResponse> findByCareertoStudent(Long studentId){
        return inscriptionToCommissionMapper.toResponseList(inscriptionToCommissionRepository.findActiveInscriptionsByStudentProgram(studentId,LocalDateTime.now()));
    }

    public void deleteInscriptionById(Long id){
        inscriptionToCommissionRepository.deleteById(id);
    }

    public void updateInscription(InscriptionToCommissionDTO dto, Long id) {
        InscriptionToCommissionEntity inscriptionToUpdate = inscriptionToCommissionRepository.findById(id).orElseThrow();
        Commission commission = commissionRepository.findById(dto.getCommissionId()).orElseThrow();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime inscriptionDate = LocalDateTime.parse(dto.getInscriptionDate(), formatter);
        LocalDateTime finishInscriptionDate = LocalDateTime.parse(dto.getFinishInscriptionDate(), formatter);

        if (inscriptionDate.isAfter(finishInscriptionDate)) {
            throw new IllegalArgumentException("La fecha del examen debe ser posterior a la fecha de inscripción.");
        }

        inscriptionToUpdate.setFinishInscriptionDate(finishInscriptionDate);
        inscriptionToUpdate.setInscriptionDate(inscriptionDate);
        inscriptionToUpdate.setCommission(commission);

        inscriptionToCommissionRepository.save(inscriptionToUpdate);
    }

}
