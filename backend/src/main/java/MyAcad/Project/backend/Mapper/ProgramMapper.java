package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Programs.Program;
import MyAcad.Project.backend.Model.Programs.ProgramResponse;
import MyAcad.Project.backend.Model.Programs.ProgramsDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProgramMapper {

    Program toEntity(ProgramsDTO programsDTO);
    ProgramsDTO toDto(Program program);

    ProgramResponse toResponse(Program program);
    List<ProgramResponse> toResponseList(List<Program> programs);

}
