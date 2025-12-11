package MyAcad.Project.backend.Model.Academic;

import MyAcad.Project.backend.Enum.AcademicStatus;
import MyAcad.Project.backend.Model.Users.Teacher;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long id;

    private String name;
    private String description;
    private int semesters;
    private boolean subjectActive;
    private AcademicStatus academicStatus;

    @ManyToMany
    @JoinTable(
            name = "subject_x_commission",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "commission_id")
    )
    private List<Commission> commissions;

    @ManyToMany
    @JoinTable(
            name = "subject_x_teacher",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Teacher> teachers;

    @ManyToMany
    @JoinTable(
            name = "subject_prerequisites",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "prerequisite_id")
    )
    private List<SubjectsEntity> prerequisites;
    //Nombre de la carrera.
    private String program;
}
