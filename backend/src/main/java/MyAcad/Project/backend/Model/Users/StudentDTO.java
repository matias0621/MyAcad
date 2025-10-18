package MyAcad.Project.backend.Model.Users;


import MyAcad.Project.backend.Enum.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class StudentDTO extends UserDTO{
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50)
    private String legajo;

    public StudentDTO(Long id, String name, String lastName, String email, String username, String password, String legajo) {
        super(id, name, lastName, email, username, password);
        this.legajo = legajo;
        this.role = Role.STUDENT;
    }
}
