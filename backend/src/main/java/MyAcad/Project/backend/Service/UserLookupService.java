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

    public Optional<? extends User> findByUsername(String username) {
        Optional<Student> student = studentRepository.findByUsername(username);
        if (student.isPresent()) return student;

        Optional<Teacher> teacher = teacherRepository.findByUsername(username);
        if (teacher.isPresent()) return student;

        return managerRepository.findByUsername(username);
    }

    public Optional<? extends User> findByEmail(String email) {
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) return student;

        Optional<Teacher> teacher = teacherRepository.findByEmail(email);
        if (teacher.isPresent()) return teacher;

        return managerRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new UserDetailsImpl(u);
    }
}
