package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission.InscriptionToCommissionDTO;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission.InscriptionToCommissionEntity;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission.InscriptionToCommissionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CommissionMapper.class, SubjectsMapper.class})
public interface InscriptionToCommissionMapper {
    InscriptionToCommissionEntity toEntity(InscriptionToCommissionDTO inscriptionToCommissionDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "commission", target = "commission")
    @Mapping(source = "inscriptionDate", target = "inscriptionDate")
    @Mapping(source = "finishInscriptionDate", target = "finishInscriptionDate")
    InscriptionToCommissionResponse toResponse(InscriptionToCommissionEntity inscriptionToCommissionEntity);
    List<InscriptionToCommissionResponse> toResponseList(List<InscriptionToCommissionEntity> inscriptionToCommissionEntityList);
}
