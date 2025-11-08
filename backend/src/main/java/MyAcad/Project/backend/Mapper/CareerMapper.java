package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Programs.Career;
import MyAcad.Project.backend.Model.Programs.CareerDTO;
import MyAcad.Project.backend.Model.Programs.CareerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CareerMapper {
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "teachers", ignore = true)
    @Mapping(target = "subjects", ignore = true)
    Career toEntity(CareerDTO dto);

    CareerDTO toDTO(Career entity);

    CareerResponse toResponse(Career entity);
    List<CareerResponse> toResponseList(List<Career> entity);
    List<CareerDTO> toDTOList(List<Career> entities);

}
