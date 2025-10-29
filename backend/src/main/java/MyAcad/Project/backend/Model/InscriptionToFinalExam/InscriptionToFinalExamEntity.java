package MyAcad.Project.backend.Model.InscriptionToFinalExam;

import MyAcad.Project.backend.Model.Subjects.SubjectsEntity;
import MyAcad.Project.backend.Model.Users.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscriptionToFinalExamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime inscriptionDate;

    private LocalDateTime finalExamDate;

    @OneToOne
    private SubjectsEntity subjects;

    @OneToMany
    private List<Student> students;



}
