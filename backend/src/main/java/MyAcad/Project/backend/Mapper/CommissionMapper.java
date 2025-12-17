package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Academic.Commission;
import MyAcad.Project.backend.Model.Academic.CommissionDTO;
import MyAcad.Project.backend.Model.Academic.CommissionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommissionMapper {

    @Mapping(target = "subjects", ignore = true)
    @Mapping(target = "teachers", ignore = true)
    @Mapping(target = "students", ignore = true)
    Commission toEntity(CommissionDTO commission);

    CommissionDTO toDTO(Commission commission);

    @Mapping(source = "students", target = "students")
    @Mapping(target = "subjects", ignore = true)
    CommissionResponse toResponse(Commission commission);
    List<CommissionResponse> toResponseList(List<Commission> commissions);
}
