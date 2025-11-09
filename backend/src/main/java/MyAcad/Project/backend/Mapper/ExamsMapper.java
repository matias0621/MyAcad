package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Academic.ExamsDTO;
import MyAcad.Project.backend.Model.Academic.ExamsEntity;
import MyAcad.Project.backend.Model.Academic.ExamsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {SubjectsMapper.class} // 👈 Reutilizamos el mapper de materias
)
public interface ExamsMapper {
    ExamsEntity toExamsEntity(ExamsDTO examsDTO);

    ExamsDTO toExamsDTO(ExamsEntity examsEntity);

    ExamsResponse toExamsResponse(ExamsEntity examsEntity);
    @Mapping(source = "subject", target = "subject")
    List<ExamsResponse> toExamsResponseList(List<ExamsEntity> examsEntities);
}

