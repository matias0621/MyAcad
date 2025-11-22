package MyAcad.Project.backend.Service.Programs;

import MyAcad.Project.backend.Enum.ProgramType;
import MyAcad.Project.backend.Exception.CareerAlreadyExistsException;
import MyAcad.Project.backend.Mapper.ProgramMapper;
import MyAcad.Project.backend.Model.Programs.*;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Repository.Programs.ProgramRepository;
import MyAcad.Project.backend.Repository.Users.StudentRepository;
import MyAcad.Project.backend.Repository.Users.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProgramService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ProgramRepository programRepository;
    private final ProgramMapper programMapper;

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

    public Page<ProgramResponse> listProgramPaginated(int page, int size) {
        Page<Program> programs = programRepository.findAll(PageRequest.of(page, size));
        return programs.map(programMapper::toResponse);
    }

    public Page<ProgramResponse> listProgramPaginatedByProgramType(int page, int size, ProgramType programType) {
        Page<Program> programs = programRepository.findByProgramType(programType,PageRequest.of(page, size));
        return programs.map(programMapper::toResponse);
    }



    public List<ProgramResponse> findByProgramType(ProgramType programType) {
        return programMapper.toResponseList(programRepository.findByProgramType(programType));
    }

    public List<ProgramResponse> findPrograms(){
        return programMapper.toResponseList(programRepository.findAll());
    }

    public ProgramResponse findProgramById(Long id) {
        return programMapper.toResponse(programRepository.findById(id).orElse(null));
    }

    public List<ProgramResponse> findByStudent(Long studentId){
        return programMapper.toResponseList(programRepository.findByStudent(studentId));
    }

    public List<ProgramResponse> findByTeacher(Long teacherId){
        return programMapper.toResponseList(programRepository.findByTeacher(teacherId));
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

    public ProgramResponse findByName(String programName){
        return programMapper.toResponse(programRepository.findByName(programName).orElseThrow());
    }

    public void deleteProgram(Long programId){
        programRepository.deleteById(programId);
    }

    public void logicDeleteProgram(Long programId){
        Program program = programRepository.findById(programId).orElseThrow();
        program.setActive(false);
        programRepository.save(program);
    }

    public void updateProgram(Long id, ProgramsDTO programUpdate){
        Program program = programRepository.findById(id).orElseThrow();
        program.setName(programUpdate.getName());
        program.setDescription(programUpdate.getDescription());
        program.setAnnualFee(programUpdate.getAnnualFee());
        program.setMonthlyFee(programUpdate.getMonthlyFee());
        program.setDurationMonths(programUpdate.getDurationMonths());
        program.setActive(programUpdate.getActive());
        programRepository.save(program);
    }

    
}
