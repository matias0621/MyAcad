package MyAcad.Project.backend.Model.InscriptionToFinalExam;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InscriptionToFinalExamDTO {

    private LocalDateTime inscriptionDate;

    private LocalDateTime finalExamDate;

    private Long subjectId;

}
