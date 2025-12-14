package MyAcad.Project.backend.Service.Programs;

import MyAcad.Project.backend.Enum.ProgramType;
import MyAcad.Project.backend.Exception.CareerAlreadyExistsException;
import MyAcad.Project.backend.Mapper.ProgramMapper;
import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
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
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProgramService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ProgramRepository programRepository;
    private final ProgramMapper programMapper;
    private final EntityManager entityManager;

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

    @Transactional
    public void deleteProgram(Long programId){
        if (!programRepository.existsById(programId)) {
            throw new RuntimeException("El programa no existe");
        }
        
        List<String> conflicts = new ArrayList<>();
        
        String[] studentTableNames = {"career_students", "course_students", "technical_students"};
        for (String tableName : studentTableNames) {
            try {
                Number count = (Number) entityManager.createNativeQuery("SELECT COUNT(*) FROM " + tableName + " WHERE program_id = :programId")
                        .setParameter("programId", programId)
                        .getSingleResult();
                if (count != null && count.longValue() > 0) {
                    @SuppressWarnings("unchecked")
                    List<Object[]> studentData = entityManager.createNativeQuery(
                            "SELECT u.name, u.last_name FROM " + tableName + " ps " +
                            "JOIN user u ON ps.user_id = u.user_id " +
                            "WHERE ps.program_id = :programId LIMIT 5")
                            .setParameter("programId", programId)
                            .getResultList();
                    
                    List<String> studentNames = studentData.stream()
                            .map(row -> String.valueOf(row[0]) + " " + String.valueOf(row[1]))
                            .toList();
                    
                    conflicts.add("Estudiantes: " + count + " estudiante(s)" + 
                            (studentNames.isEmpty() ? "" : " - " + String.join(", ", studentNames) + (count.longValue() > 5 ? " y más..." : "")));
                    break;
                }
            } catch (Exception e) {
            }
        }
        
        String[] teacherTableNames = {"career_teachers", "course_teachers", "technical_teachers"};
        for (String tableName : teacherTableNames) {
            try {
                Number count = (Number) entityManager.createNativeQuery("SELECT COUNT(*) FROM " + tableName + " WHERE program_id = :programId")
                        .setParameter("programId", programId)
                        .getSingleResult();
                if (count != null && count.longValue() > 0) {
                    @SuppressWarnings("unchecked")
                    List<Object[]> teacherData = entityManager.createNativeQuery(
                            "SELECT u.name, u.last_name FROM " + tableName + " pt " +
                            "JOIN user u ON pt.user_id = u.user_id " +
                            "WHERE pt.program_id = :programId LIMIT 5")
                            .setParameter("programId", programId)
                            .getResultList();
                    
                    List<String> teacherNames = teacherData.stream()
                            .map(row -> String.valueOf(row[0]) + " " + String.valueOf(row[1]))
                            .toList();
                    
                    conflicts.add("Profesores: " + count + " profesor(es)" + 
                            (teacherNames.isEmpty() ? "" : " - " + String.join(", ", teacherNames) + (count.longValue() > 5 ? " y más..." : "")));
                    break;
                }
            } catch (Exception e) {
            }
        }
        
        String[] subjectTableNames = {"career_subjects", "course_subjects", "technical_subjects"};
        for (String tableName : subjectTableNames) {
            try {
                Number count = (Number) entityManager.createNativeQuery("SELECT COUNT(*) FROM " + tableName + " WHERE program_id = :programId")
                        .setParameter("programId", programId)
                        .getSingleResult();
                if (count != null && count.longValue() > 0) {
                    @SuppressWarnings("unchecked")
                    List<Object> subjectData = entityManager.createNativeQuery(
                            "SELECT s.name FROM " + tableName + " ps " +
                            "JOIN subject s ON ps.subject_id = s.subject_id " +
                            "WHERE ps.program_id = :programId")
                            .setParameter("programId", programId)
                            .getResultList();
                    
                    List<String> subjectNames = subjectData.stream()
                            .map(String::valueOf)
                            .toList();
                    
                    conflicts.add("Materias: " + String.join(", ", subjectNames));
                    break;
                }
            } catch (Exception e) {
            }
        }
        
        if (!conflicts.isEmpty()) {
            String errorMsg = "No se puede eliminar el programa porque tiene: " + String.join("; ", conflicts);
            throw new RuntimeException(errorMsg);
        }
        
        deleteProgramEntity(programId);
    }
    
    private void deleteProgramEntity(Long programId) {
        String[] studentTableNames = {"career_students", "course_students", "technical_students"};
        for (String tableName : studentTableNames) {
            try {
                entityManager.createNativeQuery("DELETE FROM " + tableName + " WHERE program_id = :programId")
                        .setParameter("programId", programId)
                        .executeUpdate();
            } catch (Exception e) {
            }
        }
        
        String[] teacherTableNames = {"career_teachers", "course_teachers", "technical_teachers"};
        for (String tableName : teacherTableNames) {
            try {
                entityManager.createNativeQuery("DELETE FROM " + tableName + " WHERE program_id = :programId")
                        .setParameter("programId", programId)
                        .executeUpdate();
            } catch (Exception e) {
            }
        }
        
        String[] subjectTableNames = {"career_subjects", "course_subjects", "technical_subjects"};
        for (String tableName : subjectTableNames) {
            try {
                entityManager.createNativeQuery("DELETE FROM " + tableName + " WHERE program_id = :programId")
                        .setParameter("programId", programId)
                        .executeUpdate();
            } catch (Exception e) {
            }
        }
        
        entityManager.flush();
        programRepository.deleteById(programId);
        programRepository.flush();
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
