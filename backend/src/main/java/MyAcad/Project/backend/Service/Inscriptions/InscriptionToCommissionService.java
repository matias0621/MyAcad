package MyAcad.Project.backend.Service.Inscriptions;

import MyAcad.Project.backend.Mapper.InscriptionToCommissionMapper;
import MyAcad.Project.backend.Model.Academic.Commission;
import MyAcad.Project.backend.Model.Academic.CommissionResponse;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission.InscriptionToCommissionDTO;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission.InscriptionToCommissionEntity;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission.InscriptionToCommissionResponse;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToFinalExam.InscriptionToFinalExamDTO;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToFinalExam.InscriptionToFinalExamEntity;
import MyAcad.Project.backend.Repository.Academic.CommissionRepository;
import MyAcad.Project.backend.Repository.Inscriptions.InscriptionToCommissionRepository;
import MyAcad.Project.backend.Service.Academic.CommissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class InscriptionToCommissionService {
    private final InscriptionToCommissionRepository inscriptionToCommissionRepository;
    private final CommissionRepository commissionRepository;
    private final InscriptionToCommissionMapper inscriptionToCommissionMapper;
    private final CommissionService commissionService;

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
                .finishInscriptionDate(finishInscriptionDate)
                .inscriptionDate(inscriptionDate)
                .build();

        inscriptionToCommissionRepository.save(inscription);
    }

    public List<InscriptionToCommissionResponse> findAll(){
        List<InscriptionToCommissionResponse> listInscription = inscriptionToCommissionMapper.toResponseList(inscriptionToCommissionRepository.findAll());
        return listInscription.stream().map(this::mapSubjectInInscriptionToCommission).toList();
    }

    public List<InscriptionToCommissionResponse> findAllByCommissionId(Long commissionId){
        Commission commission = commissionRepository.findById(commissionId).orElseThrow();

        List<InscriptionToCommissionResponse> listOfInscription = inscriptionToCommissionMapper.toResponseList(inscriptionToCommissionRepository.findByCommission(commission));

        return listOfInscription.stream().map(this::mapSubjectInInscriptionToCommission).toList();
    }

    public InscriptionToCommissionResponse findById(Long id){
        InscriptionToCommissionResponse inscription = inscriptionToCommissionMapper.toResponse(inscriptionToCommissionRepository.findById(id).orElseThrow());
        return mapSubjectInInscriptionToCommission(inscription);
    }

    public List<InscriptionToCommissionResponse> findByinscriptionDate(String inscriptionDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(inscriptionDate, formatter);

        List<InscriptionToCommissionResponse> listOfInscription = inscriptionToCommissionMapper.toResponseList(inscriptionToCommissionRepository.findInscriptionToFinalExamEntitiesByInscriptionDate(localDateTime));
        return listOfInscription.stream().map(this::mapSubjectInInscriptionToCommission).toList();
    }

    public List<InscriptionToCommissionResponse> findByfinishInscription(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);

        List<InscriptionToCommissionResponse> listOfInscription = inscriptionToCommissionMapper.toResponseList(inscriptionToCommissionRepository.findInscriptionToFinalExamEntitiesByFinishInscriptionDate(localDateTime));
        return listOfInscription.stream().map(this::mapSubjectInInscriptionToCommission).toList();
    }

    public List<InscriptionToCommissionResponse> findByInscriptionDateBeforeAndFinishInscriptionDateAfter(){
        List<InscriptionToCommissionResponse> listOfInscription = inscriptionToCommissionMapper.toResponseList(inscriptionToCommissionRepository.findByInscriptionDateBeforeAndFinishInscriptionDateAfter(LocalDateTime.now(), LocalDateTime.now()));
        return listOfInscription.stream().map(this::mapSubjectInInscriptionToCommission).toList();
    }

    public List<InscriptionToCommissionResponse> findByCareertoStudent(Long studentId){
        List<InscriptionToCommissionResponse> listOfInscription = inscriptionToCommissionMapper.toResponseList(inscriptionToCommissionRepository.findActiveInscriptionsByStudentProgram(studentId,LocalDateTime.now()));
        return listOfInscription.stream().map(this::mapSubjectInInscriptionToCommission).toList();
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

    private InscriptionToCommissionResponse mapSubjectInInscriptionToCommission(InscriptionToCommissionResponse inscription){
        CommissionResponse commissionResponse = commissionService.getById(inscription.getCommission().getId()).orElseThrow();

        inscription.setCommission(commissionResponse);
        return inscription;
    }

}
