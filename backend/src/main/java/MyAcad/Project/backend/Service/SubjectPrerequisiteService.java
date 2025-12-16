package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Enum.AcademicStatus;
import MyAcad.Project.backend.Mapper.SubjectPrerequisiteMapper;
import MyAcad.Project.backend.Model.Academic.SubjectPrerequisiteDTO;
import MyAcad.Project.backend.Model.Academic.SubjectPrerequisiteEntity;
import MyAcad.Project.backend.Model.Academic.SubjectPrerequisiteResponse;
import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import MyAcad.Project.backend.Repository.Academic.SubjectsRepository;
import MyAcad.Project.backend.Repository.SubjectPrerequisiteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SubjectPrerequisiteService {
    private final SubjectPrerequisiteRepository subjectPrerequisiteRepository;
    private final SubjectPrerequisiteMapper subjectPrerequisiteMapper;
    private final SubjectsRepository subjectsRepository;

    public SubjectPrerequisiteEntity createSubjectPrerequisite(
            SubjectPrerequisiteDTO dto
    ) {
        SubjectsEntity subject =
            subjectsRepository.findById(dto.getSubjectId())
                        .orElseThrow(() -> new RuntimeException("Materia no encontrada"));

        SubjectsEntity prerequisite =
            subjectsRepository.findById(dto.getPrerequisiteId())
                        .orElseThrow(() -> new RuntimeException("Materia correlativa no encontrada"));

        if (subject.getId().equals(prerequisite.getId())) {
            throw new RuntimeException("Una materia no puede ser correlativa de sí misma");
        }

        if (subjectPrerequisiteRepository
                .existsBySubject_IdAndPrerequisite_Id(
                        subject.getId(),
                        prerequisite.getId()
                )) {
            throw new RuntimeException("La correlativa ya existe");
        }

        if (prerequisite.getSemesters() >= subject.getSemesters()) {
            throw new RuntimeException(
                    "La materia correlativa debe pertenecer a un semestre anterior"
            );
        }

        SubjectPrerequisiteEntity entity = SubjectPrerequisiteEntity.builder()
            .subject(subject)
            .prerequisite(prerequisite)
            .requiredStatus(dto.getRequiredStatus())
            .build();

        return subjectPrerequisiteRepository.save(entity);
    }

    public SubjectPrerequisiteEntity updateSubjectPrerequisite(SubjectPrerequisiteDTO subjectPrerequisite, Long idSubjectPrerequisite) {
        SubjectPrerequisiteEntity subjectPrerequisiteEntity = subjectPrerequisiteRepository.findById(idSubjectPrerequisite).orElseThrow();
        SubjectsEntity subjects = subjectsRepository.findById(subjectPrerequisite.getSubjectId()).orElseThrow();
        SubjectsEntity prerequisite = subjectsRepository.findById(subjectPrerequisite.getPrerequisiteId()).orElseThrow();

        subjectPrerequisiteEntity.setPrerequisite(prerequisite);
        subjectPrerequisiteEntity.setRequiredStatus(subjectPrerequisite.getRequiredStatus());
        subjectPrerequisiteEntity.setSubject(subjects);
        return subjectPrerequisiteRepository.save(subjectPrerequisiteEntity);
    }

    public void deleteSubjectPrerequisite(Long idSubjectPrerequisite) {
        subjectPrerequisiteRepository.deleteById(idSubjectPrerequisite);
    }

    public List<SubjectPrerequisiteResponse> findAllSubjectPrerequisites() {
        return subjectPrerequisiteMapper.toResponseList(subjectPrerequisiteRepository.findAll());
    }

    public List<SubjectPrerequisiteResponse> findAllSubjectPrerequisitesBySubjectId(Long idSubject) {
        return subjectPrerequisiteMapper.toResponseList(subjectPrerequisiteRepository.findBySubject_Id(idSubject));
    }

    public List<SubjectPrerequisiteResponse> findAllSubjectPrerequisitesByPrerequisiteId(Long idPrerequisite) {
        return subjectPrerequisiteMapper.toResponseList(subjectPrerequisiteRepository.findByPrerequisite_Id(idPrerequisite));
    }

    public List<SubjectPrerequisiteResponse> findAllSubjectPrerequisitesByRequiredStatus(AcademicStatus requiredStatus) {
        return subjectPrerequisiteMapper.toResponseList(subjectPrerequisiteRepository.findByRequiredStatus(requiredStatus));
    }

    public SubjectPrerequisiteResponse findById(Long idSubjectPrerequisite) {
        return subjectPrerequisiteMapper.toResponse(subjectPrerequisiteRepository.findById(idSubjectPrerequisite).orElseThrow());
    }

    public Boolean existsBySubjectIdAndPrerequisiteId(Long subjectId, Long prerequisiteId) {
        return subjectPrerequisiteRepository.existsBySubject_IdAndPrerequisite_Id(subjectId, prerequisiteId);
    }

    public SubjectPrerequisiteResponse findBySubjectIdAndPrerequisiteId(Long subjectId, Long prerequisiteId) {
        return subjectPrerequisiteRepository.findBySubject_IdAndPrerequisite_Id(subjectId,prerequisiteId).map(subjectPrerequisiteMapper::toResponse).orElseThrow();
    }

    public List<SubjectPrerequisiteResponse> findByProgram(String program) {
        return subjectPrerequisiteMapper.toResponseList(subjectPrerequisiteRepository.findByProgram(program));
    }

    public List<SubjectPrerequisiteResponse> findSubjectsRequiringStatus(String program, AcademicStatus status) {
        return subjectPrerequisiteMapper.toResponseList(subjectPrerequisiteRepository.findSubjectsRequiringStatus(program,status));
    }

    public void deletePrerequisite(Long subjectId, Long prerequisiteId) {
        SubjectPrerequisiteEntity s = subjectPrerequisiteRepository.findBySubject_IdAndPrerequisite_Id(subjectId, prerequisiteId).orElseThrow();
        subjectPrerequisiteRepository.delete(s);
    }
}
