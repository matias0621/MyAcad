package MyAcad.Project.backend.Model.Commission;

import MyAcad.Project.backend.Enum.ProgramType;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "commission")
@Getter @Setter @NoArgsConstructor @ToString
@AllArgsConstructor
public class Commission {
    @Id @GeneratedValue
    private Long id;
    private int number;
    private String subject;
    private String students;
    private String teachers;
    private int capacity;
    private boolean active;

    @Enumerated(EnumType.STRING)
    private ProgramType programType;

    public Commission(CommissionDTO dto) {
        this.id = dto.getId();
        this.number = dto.getNumber();
        this.subject = dto.getSubject();
        this.students = dto.getStudents();
        this.teachers = dto.getTeachers();
        this.capacity = dto.getCapacity();
        this.active = dto.isActive();
        this.programType = dto.getProgramType();
    }
}

