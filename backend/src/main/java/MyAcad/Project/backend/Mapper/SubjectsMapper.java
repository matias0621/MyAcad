package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import MyAcad.Project.backend.Model.Academic.SubjectsResponse;
import MyAcad.Project.backend.Model.Academic.SubjectPrerequisiteEntity;
import MyAcad.Project.backend.Model.Academic.SubjectPrerequisiteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectsMapper {

    // 🔹 ÚNICO método "default" (evita ambigüedad)
    @Named("basic")
    @Mapping(target = "prerequisites", ignore = true)
    SubjectsResponse toResponse(SubjectsEntity entity);

    @Named("basicList")
    List<SubjectsResponse> toResponseList(List<SubjectsEntity> entities);

    // 🔹 SOLO cuando explícitamente lo pedís
    @Named("withPrerequisites")
    SubjectsResponse toResponseWithPrerequisites(SubjectsEntity entity);
    
    // Map a SubjectPrerequisiteEntity using the basic Subjects mapping to avoid recursion
    @Mapping(source = "subject", target = "subject", qualifiedByName = "basic")
    @Mapping(source = "prerequisite", target = "prerequisite", qualifiedByName = "basic")
    SubjectPrerequisiteResponse toSubjectPrerequisiteResponse(SubjectPrerequisiteEntity entity);

    List<SubjectPrerequisiteResponse> toSubjectPrerequisiteResponseList(List<SubjectPrerequisiteEntity> entities);
}
