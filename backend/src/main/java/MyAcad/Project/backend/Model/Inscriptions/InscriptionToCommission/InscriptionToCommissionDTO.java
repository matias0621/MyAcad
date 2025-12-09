package MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission;

import MyAcad.Project.backend.Model.Academic.Commission;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InscriptionToCommissionDTO {
    private Long commissionId;
    private String inscriptionDate;
    private String finishInscriptionDate;
}
