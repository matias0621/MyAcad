package MyAcad.Project.backend.Model.Users;

import MyAcad.Project.backend.Enum.Role;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Teacher extends User{
    public Teacher(TeacherDTO dto) {
        super(dto);
        this.role = Role.TEACHER;
    }
}
