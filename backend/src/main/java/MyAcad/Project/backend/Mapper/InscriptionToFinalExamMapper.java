package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Inscriptions.InscriptionToFinalExam.InscriptionToFinalExamDTO;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToFinalExam.InscriptionToFinalExamEntity;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToFinalExam.InscriptionToFinalExamResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProgramMapper.class, SubjectsMapper.class})
public interface InscriptionToFinalExamMapper {

    @Mapping(source = "subjects", target = "subjects")
    InscriptionToFinalExamResponse toResponse(InscriptionToFinalExamEntity dto);
    @Mapping(source = "subjects", target = "subjects")
    List<InscriptionToFinalExamResponse> toResponseList(List<InscriptionToFinalExamEntity> dto);
}
