package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Configuration.UserDetailsImpl;
import MyAcad.Project.backend.Enum.Role;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Model.Users.User;
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


    public Optional<? extends User> findById(Long id, Role role) {
        return switch (role){
            case STUDENT -> studentRepository.findById(id);
            case TEACHER -> teacherRepository.findById(id);
        };
    }

    public Optional<? extends User> findByUsername(String username) {
        Optional<Student> student = studentRepository.findByUsername(username);
        if (student.isPresent()) return student;

        Optional<Teacher> teacher = teacherRepository.findByUsername(username);
        return teacher;
        //Agregar manager cuando se pueda y ponerle el if a teacher
    }

    public Optional<? extends User> findByEmail(String email) {
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) return student;

        Optional<Teacher> teacher = teacherRepository.findByEmail(email);
        return teacher;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new UserDetailsImpl(u);
    }
}
