package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Academic.SubjectsXStudentDTO;
import MyAcad.Project.backend.Model.Academic.SubjectsXStudentEntity;
import MyAcad.Project.backend.Model.Academic.SubjectsXStudentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {SubjectsMapper.class, CommissionMapper.class}
)
public interface SubjectsXStudentMapper {

    SubjectsXStudentEntity toEntity(SubjectsXStudentDTO dto);
    SubjectsXStudentDTO toDTO(SubjectsXStudentEntity entity);

    // 🔹 Forzamos BASIC para evitar ambigüedad
    @Mapping(
            target = "subjects",
            source = "subjects",
            qualifiedByName = "basic"
    )
    SubjectsXStudentResponse toResponse(SubjectsXStudentEntity entity);

    List<SubjectsXStudentResponse> toResponseList(
            List<SubjectsXStudentEntity> entities
    );
}
