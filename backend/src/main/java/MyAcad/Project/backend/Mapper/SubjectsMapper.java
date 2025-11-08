package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Academic.SubjectsDTO;
import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import MyAcad.Project.backend.Model.Academic.SubjectsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectsMapper {

    // DTO → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "commissions", ignore = true)
    @Mapping(target = "teachers", ignore = true)
    @Mapping(target = "prerequisites", ignore = true)
    SubjectsEntity toEntity(SubjectsDTO dto);

    // Entity → DTO
    SubjectsDTO toDto(SubjectsEntity entity);

    // Entity → Response básico (sin prerequisitos)
    @Named("basicResponse")
    @Mapping(target = "prerequisites", ignore = true)
    SubjectsResponse toResponse(SubjectsEntity entity);

    List<SubjectsResponse> toResponseList(List<SubjectsEntity> entities);

    // Entity → Response con prerequisitos
    @Mapping(target = "prerequisites", qualifiedByName = "mapPrerequisites")
    @Mapping(target = "program", source = "program")
    SubjectsResponse toResponseWithPrerequisites(SubjectsEntity entity);

    @Named("mapPrerequisites")
    default List<SubjectsResponse> mapPrerequisites(List<SubjectsEntity> prerequisites) {
        if (prerequisites == null || prerequisites.isEmpty()) return List.of();
        // Usamos el mapeo básico para evitar recursión infinita
        return prerequisites.stream()
                .map(this::toResponse)
                .toList();
    }
}
