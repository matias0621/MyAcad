package MyAcad.Project.backend.Model.Users;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Student extends User {
    private String legajo;

    public Student(StudentDTO dto) {
        super(dto);
    }
}
