package MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission;


import MyAcad.Project.backend.Model.Academic.Commission;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class InscriptionToCommissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inscription_commission_id")
    private Long id;
    @ManyToOne
    private Commission commission;
    private LocalDateTime inscriptionDate;
    private LocalDateTime finishInscriptionDate;

}
