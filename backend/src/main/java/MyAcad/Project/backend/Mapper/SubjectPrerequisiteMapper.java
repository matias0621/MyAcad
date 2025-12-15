package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Academic.SubjectPrerequisiteEntity;
import MyAcad.Project.backend.Model.Academic.SubjectPrerequisiteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = SubjectsMapper.class
)
public interface SubjectPrerequisiteMapper {

    @Mapping(
            target = "subject",
            source = "subject",
            qualifiedByName = "basic"
    )
    @Mapping(
            target = "prerequisite",
            source = "prerequisite",
            qualifiedByName = "basic"
    )
    SubjectPrerequisiteResponse toResponse(SubjectPrerequisiteEntity entity);

    List<SubjectPrerequisiteResponse> toResponseList(
            List<SubjectPrerequisiteEntity> entities
    );
}
