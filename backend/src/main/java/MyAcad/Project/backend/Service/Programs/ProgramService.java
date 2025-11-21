package MyAcad.Project.backend.Service.Programs;

import MyAcad.Project.backend.Enum.ProgramType;
import MyAcad.Project.backend.Exception.CareerAlreadyExistsException;
import MyAcad.Project.backend.Model.Programs.*;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Repository.Programs.CareerRepository;
import MyAcad.Project.backend.Repository.Programs.CourseRepository;
import MyAcad.Project.backend.Repository.Programs.ProgramRepository;
import MyAcad.Project.backend.Repository.Programs.TechnicalRepository;
import MyAcad.Project.backend.Repository.Users.StudentRepository;
import MyAcad.Project.backend.Repository.Users.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProgramService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ProgramRepository programRepository;

    public void createEngineering(ProgramsDTO programsDTO) {
        if (programRepository.findByName(programsDTO.getName()).isPresent()){
            throw new CareerAlreadyExistsException();
        }

        Program program = Program.builder()
                .name(programsDTO.getName())
                .description(programsDTO.getDescription())
                .annualFee(programsDTO.getAnnualFee())
                .monthlyFee(programsDTO.getMonthlyFee())
                .durationMonths(programsDTO.getDurationMonths())
                .active(programsDTO.getActive())
                .programType(ProgramType.ENGINEERING)
                .build();

        programRepository.save(program);
    }

    public void createCourse(ProgramsDTO programsDTO) {
        if (programRepository.findByName(programsDTO.getName()).isPresent()){
            throw new CareerAlreadyExistsException();
        }

        Program program = Program.builder()
                .name(programsDTO.getName())
                .description(programsDTO.getDescription())
                .annualFee(programsDTO.getAnnualFee())
                .monthlyFee(programsDTO.getMonthlyFee())
                .durationMonths(programsDTO.getDurationMonths())
                .active(programsDTO.getActive())
                .programType(ProgramType.COURSE)
                .build();

        programRepository.save(program);
    }

    public void createTechnical(ProgramsDTO programsDTO) {
        if (programRepository.findByName(programsDTO.getName()).isPresent()){
            throw new CareerAlreadyExistsException();
        }

        Program program = Program.builder()
                .name(programsDTO.getName())
                .description(programsDTO.getDescription())
                .annualFee(programsDTO.getAnnualFee())
                .monthlyFee(programsDTO.getMonthlyFee())
                .durationMonths(programsDTO.getDurationMonths())
                .active(programsDTO.getActive())
                .programType(ProgramType.TECHNICAL)
                .build();

        programRepository.save(program);
    }

    public Page<Program> listProgramPaginated(int page, int size) {
        return programRepository.findAll(PageRequest.of(page, size));
    }

    public List<Program> findByProgramType(ProgramType programType) {
        return programRepository.findByProgramType(programType);
    }

    public List<Program> findPrograms(){
        return programRepository.findAll();
    }

    public Program findProgramById(Long id) {
        return programRepository.findById(id).orElse(null);
    }

    public List<Program> findByStudent(Long studentId){
        return programRepository.findByStudent(studentId);
    }

    public List<Program> findByTeacher(Long teacherId){
        return programRepository.findByTeacher(teacherId);
    }

    public void registerStudent(String nameProgram, String legajoStudent){
        Student student = studentRepository.findByLegajo(legajoStudent).orElseThrow();
        Program program = programRepository.findByName(nameProgram).orElseThrow();

        program.getStudents().add(student);
        programRepository.save(program);
    }

    public void registerTeacher(String nameProgram, String legajoTeacher){
        Teacher teacher = teacherRepository.findByLegajo(legajoTeacher).orElseThrow();
        Program program = programRepository.findByName(nameProgram).orElseThrow();

        program.getTeachers().add(teacher);
        programRepository.save(program);
    }

    public Program findByName(String programName){
        return programRepository.findByName(programName).orElseThrow();
    }

    public void deleteProgram(Long programId){
        programRepository.deleteById(programId);
    }

    public void updateProgram(Long id, ProgramsDTO programsDTO){
        Program program = programRepository.findById(id).orElseThrow();
        program.setName(programsDTO.getName());
        program.setDescription(programsDTO.getDescription());
        program.setAnnualFee(programsDTO.getAnnualFee());
        program.setMonthlyFee(programsDTO.getMonthlyFee());
        program.setDurationMonths(programsDTO.getDurationMonths());
        program.setActive(programsDTO.getActive());
        programRepository.save(program);
    }

    
}
