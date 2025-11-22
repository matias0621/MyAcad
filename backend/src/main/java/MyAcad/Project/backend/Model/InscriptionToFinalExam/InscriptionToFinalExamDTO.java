package MyAcad.Project.backend.Model.InscriptionToFinalExam;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InscriptionToFinalExamDTO {

    private String inscriptionDate;

    private String finalExamDate;

    private Long subjectsId;

    private String program;

}
