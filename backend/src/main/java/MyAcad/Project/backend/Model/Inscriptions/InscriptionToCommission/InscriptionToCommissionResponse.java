package MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission;

import MyAcad.Project.backend.Model.Academic.CommissionResponse;

import java.time.LocalDateTime;

public class InscriptionToCommissionResponse {

    private Long id;
    private CommissionResponse commission;
    private LocalDateTime inscriptionDate;
    private LocalDateTime finishInscriptionDate;
}
