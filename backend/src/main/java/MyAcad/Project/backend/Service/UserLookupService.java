package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Configuration.UserDetailsImpl;
import MyAcad.Project.backend.Enum.Role;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Model.Users.User;
import MyAcad.Project.backend.Repository.ManagerRepository;
import MyAcad.Project.backend.Repository.StudentRepository;
import MyAcad.Project.backend.Repository.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserLookupService implements UserDetailsService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ManagerRepository managerRepository;


    public Optional<? extends User> findById(Long id, Role role) {
        return switch (role){
            case STUDENT -> studentRepository.findById(id);
            case TEACHER -> teacherRepository.findById(id);
            case MANAGER -> managerRepository.findById(id);
        };
    }

    public Optional<? extends User> findByLegajo(String legajo) {
        Optional<Student> student = studentRepository.findByLegajo(legajo);
        if (student.isPresent()) return student;

        Optional<Teacher> teacher = teacherRepository.findByLegajo(legajo);
        if (teacher.isPresent()) return teacher;

        return managerRepository.findByLegajo(legajo);
    }

    public Optional<? extends User> findByEmail(String email) {
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) return student;

        Optional<Teacher> teacher = teacherRepository.findByEmail(email);
        if (teacher.isPresent()) return teacher;

        return managerRepository.findByEmail(email);
    }

    //Se llama byUsername por defecto por el override, pero utilizamos legajo para el login
    @Override
    public UserDetails loadUserByUsername(String legajo) throws UsernameNotFoundException {
        User u = findByLegajo(legajo)
                .orElseThrow(() -> new UsernameNotFoundException("Legajo no encontrado"));
        return new UserDetailsImpl(u);
    }


}
