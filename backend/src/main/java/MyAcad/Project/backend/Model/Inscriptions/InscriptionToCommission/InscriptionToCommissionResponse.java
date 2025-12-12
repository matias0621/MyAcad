package MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission;

import MyAcad.Project.backend.Model.Academic.CommissionResponse;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InscriptionToCommissionResponse {

    private Long id;
    private CommissionResponse commission;
    private LocalDateTime inscriptionDate;
    private LocalDateTime finishInscriptionDate;
}
