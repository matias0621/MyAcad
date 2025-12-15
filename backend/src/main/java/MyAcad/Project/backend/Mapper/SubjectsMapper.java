package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import MyAcad.Project.backend.Model.Academic.SubjectsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectsMapper {

    // 🔹 Mapeo básico de Subject -> SubjectsResponse (sin prerequisitos)
    @Named("basic")
    @Mapping(target = "prerequisites", ignore = true)
    SubjectsResponse toResponse(SubjectsEntity entity);

    @Named("basicList")
    @Mapping(target = "prerequisites", ignore = true)
    List<SubjectsResponse> toResponseList(List<SubjectsEntity> entities);

    // 🔹 Método alternativo, por ahora también sin prerrequisitos (se llenan manualmente en el service)
    @Named("withPrerequisites")
    @Mapping(target = "prerequisites", ignore = true)
    SubjectsResponse toResponseWithPrerequisites(SubjectsEntity entity);
}
