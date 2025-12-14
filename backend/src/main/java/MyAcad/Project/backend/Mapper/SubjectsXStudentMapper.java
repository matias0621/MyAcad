package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Academic.SubjectsXStudentDTO;
import MyAcad.Project.backend.Model.Academic.SubjectsXStudentEntity;
import MyAcad.Project.backend.Model.Academic.SubjectsXStudentResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SubjectsMapper.class, CommissionMapper.class})
public interface SubjectsXStudentMapper {

    SubjectsXStudentEntity toEntity(SubjectsXStudentDTO subjectsXStudentDTO);
    SubjectsXStudentDTO toDTO(SubjectsXStudentEntity subjectsXStudentEntity);

    SubjectsXStudentResponse toResponse(SubjectsXStudentEntity subjectsXStudent);
    List<SubjectsXStudentResponse> toResponseList(List<SubjectsXStudentEntity> subjectsXStudentDTOList);
}
