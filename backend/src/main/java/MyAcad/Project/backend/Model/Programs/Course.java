package MyAcad.Project.backend.Model.Programs;

import MyAcad.Project.backend.Enum.ProgramType;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Course extends Program {

    public Course(CourseDTO dto){
        super(dto);
        this.programType = ProgramType.COURSE;
    }


}
