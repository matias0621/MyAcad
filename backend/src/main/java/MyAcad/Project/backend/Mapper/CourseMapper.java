package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Programs.Course;
import MyAcad.Project.backend.Model.Programs.CourseDTO;
import MyAcad.Project.backend.Model.Programs.CourseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "students", ignore = true)
    @Mapping(target = "teachers", ignore = true)
    @Mapping(target = "subjects", ignore = true)
    Course toEntity(CourseDTO dto);

    CourseDTO toDTO(Course entity);

    CourseResponse toResponse(Course entity);
    List<CourseResponse> toResponseList(List<Course> entityList);

    List<CourseDTO> toDTOList(List<Course> entities);
}
