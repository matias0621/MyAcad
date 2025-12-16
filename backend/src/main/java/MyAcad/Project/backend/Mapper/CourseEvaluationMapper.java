package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Academic.CourseEvaluationEntity;
import MyAcad.Project.backend.Model.Academic.CourseEvaluationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TeacherMapper.class})
public interface CourseEvaluationMapper {
    @Mapping(target = "subject", ignore = true)
    CourseEvaluationResponse toResponse(CourseEvaluationEntity entity);

    @Mapping(target = "subject", ignore = true)
    List<CourseEvaluationResponse> toResponseList(List<CourseEvaluationEntity> entities);
}
