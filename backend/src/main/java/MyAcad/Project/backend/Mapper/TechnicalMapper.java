package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Programs.Technical;
import MyAcad.Project.backend.Model.Programs.TechnicalDTO;
import MyAcad.Project.backend.Model.Programs.TechnicalResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TechnicalMapper {
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "teachers", ignore = true)
    @Mapping(target = "subjects", ignore = true)
    Technical toEntity(TechnicalDTO dto);

    TechnicalDTO toDTO(Technical entity);

    TechnicalResponse toResponse(Technical entity);
    List<TechnicalResponse> toResponseList(List<Technical> entities);

    List<TechnicalDTO> toDTOList(List<Technical> entities);
}
