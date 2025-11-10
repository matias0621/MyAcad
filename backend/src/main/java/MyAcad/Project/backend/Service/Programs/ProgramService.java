package MyAcad.Project.backend.Service.Programs;

import MyAcad.Project.backend.Model.Programs.Career;
import MyAcad.Project.backend.Model.Programs.Course;
import MyAcad.Project.backend.Model.Programs.Program;
import MyAcad.Project.backend.Model.Programs.Technical;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Repository.Programs.CareerRepository;
import MyAcad.Project.backend.Repository.Programs.CourseRepository;
import MyAcad.Project.backend.Repository.Programs.TechnicalRepository;
import MyAcad.Project.backend.Repository.Users.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProgramService {
    private final CourseRepository courseRepository;
    private final TechnicalRepository technicalRepository;
    private final CareerRepository careerRepository;
    private final StudentRepository studentRepository;

    public List<Program> findPrograms(){
        List<Program> programs = new ArrayList<>();
        programs.addAll(careerRepository.findAll());
        programs.addAll(technicalRepository.findAll());
        programs.addAll(courseRepository.findAll());
        return programs;
    }

    public List<Program> findByStudent(Long studentId){

        List<Program> programs = new ArrayList<>();

        programs.addAll(careerRepository.findByStudent(studentId));
        programs.addAll(technicalRepository.findByStudent(studentId));
        programs.addAll(courseRepository.findByStudent(studentId));

        return programs;
    }

    public List<Program> findByTeacher(Long teacherId){
        List<Program> programs = new ArrayList<>();

        programs.addAll(careerRepository.findByTeacher(teacherId));
        programs.addAll(technicalRepository.findByTeacher(teacherId));
        programs.addAll(courseRepository.findByTeacher(teacherId));

        return programs;
    }

    public void registerStudent(String nameProgram, String legajoStudent){
        Student student = studentRepository.findByLegajo(legajoStudent).orElseThrow();
        Program program = findByName(nameProgram);

        program.getStudents().add(student);

        switch (program) {
            case Career career -> careerRepository.save(career);
            case Technical technical -> technicalRepository.save(technical);
            case Course course -> courseRepository.save(course);
            default -> throw new RuntimeException("No existe esa materia");
        }
    }

    public Program findByName(String name){
        return  careerRepository.findByName(name)
                .map(p -> (Program) p)
                .or(() -> courseRepository.findByName(name).map(p -> (Program) p))
                .or(() -> technicalRepository.findByName(name).map(p -> (Program) p))
                .orElseThrow(() -> new RuntimeException("Program not found"));
    }
}
