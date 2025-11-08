package MyAcad.Project.backend.Model.Academic;

import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.Teacher;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "commission")
@Getter @Setter @NoArgsConstructor @ToString
@AllArgsConstructor
public class Commission {
    @Id
    @GeneratedValue
    @Column(name = "commission_id")
    private Long id;
    private int number;

    @ManyToMany
    @JoinTable(
            name = "subject_x_commission",
            joinColumns = @JoinColumn(name = "commission_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<SubjectsEntity> subjects;
    
    @ManyToMany
    @JoinTable(
            name = "teacher_x_commission",
            joinColumns = @JoinColumn(name = "commission_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Teacher> teachers;
    
    @ManyToMany
    @JoinTable(
            name = "students_in_commission",
            joinColumns = @JoinColumn(name = "commission_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Student> students;
    private String program;
    private int capacity;
    private boolean active;

    public Commission(CommissionDTO dto) {
        this.id = dto.getId();
        this.number = dto.getNumber();
        this.program = dto.getProgram();
        this.capacity = dto.getCapacity();
        this.active = dto.isActive();

        this.subjects = new ArrayList<>();
        this.teachers = new ArrayList<>();
        this.students = new ArrayList<>();
    }
}

