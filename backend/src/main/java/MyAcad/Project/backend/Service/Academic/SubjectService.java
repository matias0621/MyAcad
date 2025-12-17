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
                .subjectActive(subject.getSubjectActive())
                .academicStatus(subject.getAcademicStatus())
                .program(subject.getProgram())
                .build();


        subjectsRepository.save(s);
    }

    public List<SubjectsResponse> getAllSubjects() {
        return subjectsRepository.findAll()
                .stream()
                .map(this::mapSubjectManually)
                .toList();
    }

    public Page<SubjectsResponse> listSubject(int page, int size) {
        Page<SubjectsEntity> subjectsPage = subjectsRepository.findAll(PageRequest.of(page, size));
        List<SubjectsResponse> responseList = subjectsPage.getContent()
                .stream()
                .map(this::mapSubjectManually)
                .toList();

        return new PageImpl<>(
                responseList,
                subjectsPage.getPageable(),
                subjectsPage.getTotalElements()
        );
    }

    public List<SubjectsResponse> getByNameIgnoringCase(String name) {
        return subjectsRepository.findByNameContainingIgnoreCase(name).stream().map(this::mapSubjectManually).toList();
    }
    public List<SubjectsResponse> findByProgram(String program) {
        return subjectsRepository.findByProgram(program).stream().map(this::mapSubjectManually).toList();
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
        existingSubject.setSubjectActive(updatedSubject.isSubjectActive());

        subjectsRepository.save(existingSubject);
    }

    public List<SubjectsResponse> findByProgramAndSemesterLessThan(String programName, int semester) {
        return subjectsRepository.findByProgramAndSemestersLessThan(programName, semester).stream().map(this::mapSubjectManually).toList();
    }

    public List<SubjectsResponse> findBySemestersLessThan(Integer semesters) {
        return subjectsRepository.findBySemestersLessThan(semesters).stream().map(this::mapSubjectManually).toList();
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

    public SubjectsResponse mapSubjectManually(SubjectsEntity entity) {
        if (entity == null) return null;

        SubjectsResponse res = new SubjectsResponse();
        res.setId(entity.getId());
        res.setName(entity.getName());
        res.setDescription(entity.getDescription());
        res.setSemesters(entity.getSemesters());
        res.setSubjectActive(entity.isSubjectActive());
        res.setAcademicStatus(entity.getAcademicStatus());
        res.setProgram(entity.getProgram());

        // Mapear prerrequisitos a mano (un solo nivel)
        if (entity.getPrerequisites() != null) {
            List<SubjectPrerequisiteResponse> prereqResponses = entity.getPrerequisites().stream()
                    .map(sp -> {
                        SubjectPrerequisiteResponse r = new SubjectPrerequisiteResponse();
                        r.setId((long) sp.getId());
                        r.setRequiredStatus(sp.getRequiredStatus());

                        // subject
                        SubjectsEntity subject = sp.getSubject();
                        if (subject != null) {
                            SubjectsResponse sr = new SubjectsResponse();
                            sr.setId(subject.getId());
                            sr.setName(subject.getName());
                            sr.setDescription(subject.getDescription());
                            sr.setSemesters(subject.getSemesters());
                            sr.setSubjectActive(subject.isSubjectActive());
                            sr.setAcademicStatus(subject.getAcademicStatus());
                            sr.setProgram(subject.getProgram());
                            r.setSubject(sr);
                        }

                        // prerequisite
                        SubjectsEntity pre = sp.getPrerequisite();
                        if (pre != null) {
                            SubjectsResponse pr = new SubjectsResponse();
                            pr.setId(pre.getId());
                            pr.setName(pre.getName());
                            pr.setDescription(pre.getDescription());
                            pr.setSemesters(pre.getSemesters());
                            pr.setSubjectActive(pre.isSubjectActive());
                            pr.setAcademicStatus(pre.getAcademicStatus());
                            pr.setProgram(pre.getProgram());
                            r.setPrerequisite(pr);
                        }

                        return r;
                    })
                    .toList();

            res.setPrerequisites(prereqResponses);
        }

        return res;
    }

}
