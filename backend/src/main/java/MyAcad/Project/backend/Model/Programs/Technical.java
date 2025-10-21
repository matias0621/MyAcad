package MyAcad.Project.backend.Model.Programs;

import MyAcad.Project.backend.Enum.ProgramType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Technical extends Program {

    public Technical(TechnicalDTO dto){
        super(dto);
        this.programType = ProgramType.TECHNICAL;
    }
}
