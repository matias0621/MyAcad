package MyAcad.Project.backend.Model.Commission;

import MyAcad.Project.backend.Enum.ProgramType;
import MyAcad.Project.backend.Model.Subjects.SubjectsEntity;
import MyAcad.Project.backend.Model.Users.Student;
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
    private Long id;
    private int number;

    @ManyToMany(

    )
    private Set<SubjectsEntity> subject;
    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;
    private String teachers;
    private int capacity;
    private boolean active;

    @Enumerated(EnumType.STRING)
    private ProgramType programType;

}

