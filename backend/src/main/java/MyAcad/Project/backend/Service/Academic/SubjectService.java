package MyAcad.Project.backend.Service.Academic;

import MyAcad.Project.backend.Exception.NameMateriaAlreadyExistsException;
import MyAcad.Project.backend.Mapper.SubjectsMapper;
import MyAcad.Project.backend.Model.Academic.SubjectsDTO;
import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import MyAcad.Project.backend.Model.Academic.SubjectsResponse;
import MyAcad.Project.backend.Model.Programs.Career;
import MyAcad.Project.backend.Model.Programs.Course;
import MyAcad.Project.backend.Model.Programs.Program;
import MyAcad.Project.backend.Model.Programs.Technical;
import MyAcad.Project.backend.Repository.Academic.SubjectsRepository;
import MyAcad.Project.backend.Repository.Programs.CareerRepository;
import MyAcad.Project.backend.Repository.Programs.CourseRepository;
import MyAcad.Project.backend.Repository.Programs.TechnicalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectsRepository subjectsRepository;
    private final SubjectsMapper subjectsMapper;
    private final CareerRepository careerRepository;
    private final TechnicalRepository technicalRepository;
    private final CourseRepository courseRepository;

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

    public Page<SubjectsEntity> listSubject(int page, int size) {
       return subjectsRepository.findBySubjectActiveTrue(PageRequest.of(page, size));
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

    public ResponseEntity<Void> definitiveDeleteSubject(Long subjectId) {
        if (!subjectsRepository.existsById(subjectId)) {
            return ResponseEntity.notFound().build();
        }
        subjectsRepository.deleteById(subjectId);
        return ResponseEntity.ok().build();
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

        for (int i = 0; i < subjectPrerequisiteId.size(); i++) {
            SubjectsEntity subjectsPrerequisite = subjectsRepository.findById(subjectPrerequisiteId.get(i)).orElseThrow();

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
        return  careerRepository.findByName(name)
                .map(p -> (Program) p)
                .or(() -> courseRepository.findByName(name).map(p -> (Program) p))
                .or(() -> technicalRepository.findByName(name).map(p -> (Program) p))
                .orElseThrow(() -> new RuntimeException("Program not found"));
    }

    public void addSubjectsToCareer(String nameCareer, SubjectsEntity subjectsEntity) {
        Program program = findProgramByName(nameCareer);

        program.getSubjects().add(subjectsEntity);

        switch (program) {
            case Career career -> careerRepository.save(career);
            case Technical technical -> technicalRepository.save(technical);
            case Course course -> courseRepository.save(course);
            default -> throw new RuntimeException("No existe esa materia");
        }
    }

    public void deleteSubjectsToCareer(String nameCareer, SubjectsEntity subjectsEntity){
        Program program = findProgramByName(nameCareer);

        program.getSubjects().remove(subjectsEntity);

        switch (program) {
            case Career career -> careerRepository.save(career);
            case Technical technical -> technicalRepository.save(technical);
            case Course course -> courseRepository.save(course);
            default -> throw new RuntimeException("No existe esa materia");
        }
    }

}
