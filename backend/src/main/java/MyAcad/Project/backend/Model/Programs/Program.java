package MyAcad.Project.backend.Model.Programs;

import MyAcad.Project.backend.Enum.ProgramType;
import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.Teacher;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@ToString
public abstract class Program {

    @Id
    @GeneratedValue
    @Column(name = "program_id")
    private Long id;
    private String name;
    private String description;
    private Integer durationMonths;
    private Double monthlyFee;
    private Double annualFee;
    private Boolean active;

    @Enumerated(EnumType.STRING)
    protected ProgramType programType;

    public Program(ProgramsDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.durationMonths = dto.getDurationMonths();
        this.monthlyFee = dto.getMonthlyFee();
        this.annualFee = dto.getAnnualFee();
        this.active = dto.getActive();
        this.programType = dto.getProgramType();
    }

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "program_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Student> students;


    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "program_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Teacher> teachers;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "program_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @JsonManagedReference("program-subjects")
    private Set<SubjectsEntity> subjects;

}
