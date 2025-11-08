package MyAcad.Project.backend.Model.Users;

import MyAcad.Project.backend.Enum.Role;
import MyAcad.Project.backend.Model.Academic.Commission;
import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Teacher extends User{
    @ManyToMany
    @JoinTable(
            name = "subject_x_teacher",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<SubjectsEntity> subjects;

    @ManyToMany
    @JoinTable(
            name = "teacher_x_commission",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "commission_id")
    )
    private List<Commission> commissions;

    public Teacher(TeacherDTO dto) {
        super(dto);
        this.role = Role.TEACHER;
    }
}
