package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Academic.ExamsDTO;
import MyAcad.Project.backend.Model.Academic.ExamsEntity;
import MyAcad.Project.backend.Model.Academic.ExamsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface ExamsMapper {
    ExamsEntity toExamsEntity(ExamsDTO examsDTO);

    ExamsDTO toExamsDTO(ExamsEntity examsEntity);

    @Mapping(target = "subject", ignore = true)
    ExamsResponse toExamsResponse(ExamsEntity examsEntity);
    List<ExamsResponse> toExamsResponseList(List<ExamsEntity> examsEntities);
}

