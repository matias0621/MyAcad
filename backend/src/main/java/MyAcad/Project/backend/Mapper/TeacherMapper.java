package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Model.Users.TeacherDTO;
import MyAcad.Project.backend.Model.Users.TeacherResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SubjectsMapper.class, CommissionMapper.class})
public interface TeacherMapper {

    Teacher toEntity(TeacherDTO teacher);
    @Mapping(source = "active", target = "active")
    TeacherResponse toResponse(Teacher teacher);

    List<TeacherResponse> toResponseList(List<Teacher> teachers);
}
