package MyAcad.Project.backend.Model.Commission;

import MyAcad.Project.backend.Enum.ProgramType;
import MyAcad.Project.backend.Model.Subjects.SubjectsEntity;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;


@Entity
@Table(name = "commission")
@Getter @Setter @NoArgsConstructor @ToString
@AllArgsConstructor
public class Commission {
    @Id @GeneratedValue
    @Column(name = "commision_id")
    private Long id;
    private int number;

    @ManyToMany
    @JoinTable(
            name = "subject_in_commission",
            joinColumns = @JoinColumn(name = "commision_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<SubjectsEntity> subject;
    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;
    @OneToOne
    private Teacher teachers;
    @OneToMany
    private List<Teacher> collaborator;
    private int capacity;
    private boolean active;

    @Enumerated(EnumType.STRING)
    private ProgramType programType;

    public Commission(CommissionDTO dto) {
    }
}

