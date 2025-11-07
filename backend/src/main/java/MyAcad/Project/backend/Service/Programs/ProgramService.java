package MyAcad.Project.backend.Service.Programs;

import MyAcad.Project.backend.Model.Programs.Program;
import MyAcad.Project.backend.Repository.Programs.CareerRepository;
import MyAcad.Project.backend.Repository.Programs.CourseRepository;
import MyAcad.Project.backend.Repository.Programs.TechnicalRepository;
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
}
