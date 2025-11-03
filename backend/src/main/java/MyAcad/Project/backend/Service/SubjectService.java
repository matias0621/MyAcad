package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Exception.NameMateriaAlreadyExistsException;
import MyAcad.Project.backend.Model.Subjects.SubjectsDTO;
import MyAcad.Project.backend.Model.Subjects.SubjectsEntity;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Repository.SubjectsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.security.auth.Subject;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectsRepository subjectsRepository;

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
                .build();

        subjectsRepository.save(s);
    }

    public List<SubjectsEntity> getAllSubjects() {
        return subjectsRepository.findAll();
    }

    public Page<SubjectsEntity> listSubject(int page, int size) {
       return subjectsRepository.findBySubjectActiveTrue(PageRequest.of(page, size));
    }

    public List<SubjectsEntity> getByNameIgnoringCase(String name) {
        return subjectsRepository.findByNameContainingIgnoreCase(name);
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

    public Optional<SubjectsEntity> getById(Long subjectId) {
        return subjectsRepository.findById(subjectId);
    }

    public ResponseEntity<SubjectsEntity> modifySubject(Long subjectId, SubjectsEntity updatedSubject) {
        Optional<SubjectsEntity> existingSubjectOpt = subjectsRepository.findById(subjectId);

        if (existingSubjectOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        SubjectsEntity existingSubject = existingSubjectOpt.get();

        Optional<SubjectsEntity> subjectWithSameName = subjectsRepository.findByName(updatedSubject.getName());
        if (subjectWithSameName.isPresent() && !subjectWithSameName.get().getId().equals(subjectId)) {
            throw new NameMateriaAlreadyExistsException();
        }

        existingSubject.setName(updatedSubject.getName());
        existingSubject.setDescription(updatedSubject.getDescription());
        existingSubject.setSemesters(updatedSubject.getSemesters());
        existingSubject.setPrerequisites(updatedSubject.getPrerequisites());

        subjectsRepository.save(existingSubject);
        return ResponseEntity.ok(existingSubject);
    }

    public void addPrerequisite(List<Long> subjectPrerequisiteId, Long subjectId) {
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

        subjects.getPrerequisites().remove(subjects);
        subjectsRepository.save(subjects);
    }

}
