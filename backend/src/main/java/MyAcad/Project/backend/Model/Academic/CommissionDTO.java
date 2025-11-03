package MyAcad.Project.backend.Model.Academic;

import MyAcad.Project.backend.Model.Users.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CommissionDTO {

    private Long id;
    private int number;

    private List<SubjectsEntity> subjects;

    // IDs de los estudiantes
    private List<Student> students;

    //Nombre de la carrera.
    private String program;

    private int capacity;
    private boolean active;
}
