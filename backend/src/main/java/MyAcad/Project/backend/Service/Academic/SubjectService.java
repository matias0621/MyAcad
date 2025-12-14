package MyAcad.Project.backend.Service.Academic;

import MyAcad.Project.backend.Exception.NameMateriaAlreadyExistsException;
import MyAcad.Project.backend.Mapper.SubjectsMapper;
import MyAcad.Project.backend.Model.Academic.*;
import MyAcad.Project.backend.Model.Programs.Program;
import MyAcad.Project.backend.Repository.Academic.SubjectsRepository;
import MyAcad.Project.backend.Repository.Programs.ProgramRepository;
import MyAcad.Project.backend.Repository.SubjectsXStudentRepository;
import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectsRepository subjectsRepository;
    private final SubjectsMapper subjectsMapper;
    private final ProgramRepository programRepository;
    private final SubjectsXStudentRepository subjectsXStudentRepository;

    public void createSubject(SubjectsDTO subject) {
        if (subjectsRepository.findByName(subject.getName()).isPresent()){
            throw new NameMateriaAlreadyExistsException();
        }

        SubjectsEntity s = SubjectsEntity.builder()
                .name(subject.getName())
                .description(subject.getDescription())
                .semesters(subject.getSemesters())
                .prerequisites(subject.getPrerequisites())
                .subjectActive(subject.getSubjectActive())
                .academicStatus(subject.getAcademicStatus())
                .program(subject.getProgram())
                .build();


        subjectsRepository.save(s);
    }

    public List<SubjectsResponse> getAllSubjects() {
        return subjectsRepository.findAll()
                .stream()
                .map(subjectsMapper::toResponseWithPrerequisites)
                .toList();
    }

    public Page<SubjectsResponse> listSubject(int page, int size) {
        Page<SubjectsEntity> subjectsPage = subjectsRepository.findAll(PageRequest.of(page, size));
        List<SubjectsResponse> responseList = subjectsPage.getContent()
                .stream()
                .map(subjectsMapper::toResponseWithPrerequisites)
                .toList();

        return new PageImpl<>(
                responseList,
                subjectsPage.getPageable(),
                subjectsPage.getTotalElements()
        );
    }

    public List<SubjectsResponse> getByNameIgnoringCase(String name) {
        return subjectsMapper.toResponseList(subjectsRepository.findByNameContainingIgnoreCase(name));
    }
    public List<SubjectsResponse> findByProgram(String program) {
        return subjectsMapper.toResponseList(subjectsRepository.findByProgram(program));
    }

    public ResponseEntity<Void> deleteSubject(Long subjectId) {
        if (!subjectsRepository.existsById(subjectId)) {
            return ResponseEntity.notFound().build();
        }
        SubjectsEntity subject = subjectsRepository.findById(subjectId).orElseThrow();
        subject.setSubjectActive(false);
        subjectsRepository.save(subject);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<Void> definitiveDeleteSubject(Long subjectId) {
        if (!subjectsRepository.existsById(subjectId)) {
            return ResponseEntity.notFound().build();
        }
        
        Optional<SubjectsEntity> subjectOpt = subjectsRepository.findById(subjectId);
        if (subjectOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        SubjectsEntity subject = subjectOpt.get();
        List<String> conflicts = new ArrayList<>();
        
        if (subject.getCommissions() != null && !subject.getCommissions().isEmpty()) {
            List<String> commissionNumbers = subject.getCommissions().stream()
                    .filter(c -> c != null && c.getNumber() != 0)
                    .map(c -> "Comisión " + c.getNumber() + " (" + c.getProgram() + ")")
                    .toList();
            if (!commissionNumbers.isEmpty()) {
                conflicts.add("Comisiones: " + String.join(", ", commissionNumbers));
            }
        }
        
        if (subject.getTeachers() != null && !subject.getTeachers().isEmpty()) {
            List<String> teacherNames = subject.getTeachers().stream()
                    .filter(t -> t != null && t.getName() != null && t.getLastName() != null)
                    .map(t -> t.getName() + " " + t.getLastName())
                    .toList();
            if (!teacherNames.isEmpty()) {
                conflicts.add("Profesores: " + teacherNames.size() + " profesor(es) - " + String.join(", ", teacherNames.stream().limit(5).toList()) + (teacherNames.size() > 5 ? " y más..." : ""));
            }
        }
        
        List<SubjectsXStudentEntity> subjectsXStudent = subjectsXStudentRepository.findAll().stream()
                .filter(sxs -> sxs.getSubjects() != null && sxs.getSubjects().getId() != null && sxs.getSubjects().getId().equals(subjectId))
                .toList();
        if (!subjectsXStudent.isEmpty()) {
            conflicts.add("Inscripciones de estudiantes: " + subjectsXStudent.size() + " inscripción(es)");
        }
        
        List<Program> programs = programRepository.findAll().stream()
                .filter(p -> p.getSubjects() != null && 
                        p.getSubjects().stream()
                                .anyMatch(s -> s != null && s.getId() != null && s.getId().equals(subjectId)))
                .toList();
        if (!programs.isEmpty()) {
            List<String> programNames = programs.stream()
                    .filter(p -> p != null && p.getName() != null)
                    .map(Program::getName)
                    .toList();
            if (!programNames.isEmpty()) {
                conflicts.add("Programas: " + String.join(", ", programNames));
            }
        }
        
        if (!conflicts.isEmpty()) {
            String errorMsg = "No se puede eliminar la materia porque está asociada a: " + String.join("; ", conflicts);
            throw new RuntimeException(errorMsg);
        }
        
        try {
            subjectsRepository.deleteById(subjectId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al eliminar la materia: " + e.getMessage(), e);
        }
    }

    public Optional<SubjectsEntity> getById(Long subjectId) {
        return subjectsRepository.findById(subjectId);
    }

    public void modifySubject(Long subjectId, SubjectsEntity updatedSubject) {
        Optional<SubjectsEntity> existingSubjectOpt = subjectsRepository.findById(subjectId);

        if (existingSubjectOpt.isEmpty()) {
            throw new RuntimeException("No existe la materia");
        }

        SubjectsEntity existingSubject = existingSubjectOpt.get();

        Optional<SubjectsEntity> subjectWithSameName = subjectsRepository.findByName(updatedSubject.getName());
        if (subjectWithSameName.isPresent() && !subjectWithSameName.get().getId().equals(subjectId)) {
            throw new NameMateriaAlreadyExistsException();
        }

        existingSubject.setName(updatedSubject.getName());
        existingSubject.setDescription(updatedSubject.getDescription());
        existingSubject.setSemesters(updatedSubject.getSemesters());
        existingSubject.setAcademicStatus(updatedSubject.getAcademicStatus());
        existingSubject.setProgram(updatedSubject.getProgram());
        existingSubject.setPrerequisites(updatedSubject.getPrerequisites());
        existingSubject.setSubjectActive(updatedSubject.isSubjectActive());

        subjectsRepository.save(existingSubject);
    }

    public List<SubjectsResponse> findByProgramAndSemesterLessThan(String programName, int semester) {
        return subjectsMapper.toResponseList(subjectsRepository.findByProgramAndSemestersLessThan(programName, semester));
    }

    public void addPrerequisite(Long subjectPrerequisiteId, Long subjectId) {
        SubjectsEntity subjects = subjectsRepository.findById(subjectId).orElseThrow();
        SubjectsEntity subjectsPrerequisite = subjectsRepository.findById(subjectPrerequisiteId).orElseThrow();

        if (subjects.getPrerequisites().contains(subjectsPrerequisite)) {
            throw new NameMateriaAlreadyExistsException();
        }

        subjects.getPrerequisites().add(subjectsPrerequisite);
        subjectsRepository.save(subjects);
    }

    public void addPrerequisiteList(List<Long> subjectPrerequisiteId, Long subjectId) {
        SubjectsEntity subjects = subjectsRepository.findById(subjectId).orElseThrow();

        for (Long aLong : subjectPrerequisiteId) {
            SubjectsEntity subjectsPrerequisite = subjectsRepository.findById(aLong).orElseThrow();

            if (subjectsPrerequisite.getPrerequisites().contains(subjects)) {
                throw new NameMateriaAlreadyExistsException();
            }

            if (subjects.getSemesters() < subjectsPrerequisite.getSemesters()) {
                throw new RuntimeException("you can't do prerequisite a subject of semester upper");
            }

            subjects.getPrerequisites().add(subjects);
        }

        subjectsRepository.save(subjects);
    }

    public void deleteAPrerequisite(Long subjectPrerequisiteId, Long subjectId) {
        SubjectsEntity subjectsPrerequisite = subjectsRepository.findById(subjectPrerequisiteId).orElseThrow();
        SubjectsEntity subjects = subjectsRepository.findById(subjectId).orElseThrow();

        if (!subjects.getPrerequisites().contains(subjectsPrerequisite)){
            throw new NameMateriaAlreadyExistsException();
        }
        subjects.getPrerequisites().remove(subjectsPrerequisite);
        subjectsRepository.save(subjects);
    }

    public List<SubjectsResponse> findBySemestersLessThan(Integer semesters) {
        return subjectsMapper.toResponseList(subjectsRepository.findBySemestersLessThan(semesters));
    }

    public Program findProgramByName(String name) {
        return  programRepository.findByName(name).orElseThrow();
    }

    public void addSubjectsToCareer(String nameCareer, SubjectsEntity subjectsEntity) {
        Program program = findProgramByName(nameCareer);

        program.getSubjects().add(subjectsEntity);

        programRepository.save(program);
    }

    public void deleteSubjectsToCareer(String nameCareer, SubjectsEntity subjectsEntity){
        Program program = findProgramByName(nameCareer);

        program.getSubjects().remove(subjectsEntity);

        programRepository.save(program);
    }

}
