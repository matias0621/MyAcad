package MyAcad.Project.backend.Model.Users;

import MyAcad.Project.backend.Enum.Role;
import MyAcad.Project.backend.Model.Commission.Commission;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student extends User {

    @ManyToOne
    @JoinColumn(name = "commission_id")
    private Commission commission;

    public Student(StudentDTO dto) {
        super(dto);
        this.role = Role.STUDENT;
    }

}
