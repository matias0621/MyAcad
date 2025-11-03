package MyAcad.Project.backend.Model.InscriptionFinalExam;

import MyAcad.Project.backend.Model.Subjects.SubjectsEntity;
import MyAcad.Project.backend.Model.Users.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InscriptionFinalExamEnitity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inscriptionFinalExam_id")
    private Long id;

    private LocalDate dateOfExam;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "inscriptionFinalExam_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Student> listOfStudents;

    @OneToOne
    private SubjectsEntity subjects;


}
