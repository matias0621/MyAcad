package MyAcad.Project.backend.Mapper;

import MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission.InscriptionToCommissionDTO;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission.InscriptionToCommissionEntity;
import MyAcad.Project.backend.Model.Inscriptions.InscriptionToCommission.InscriptionToCommissionResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = CommissionMapper.class)
public interface InscriptionToCommissionMapper {
    InscriptionToCommissionEntity toEntity(InscriptionToCommissionDTO inscriptionToCommissionDTO);

    InscriptionToCommissionResponse toResponse(InscriptionToCommissionEntity inscriptionToCommissionEntity);
    List<InscriptionToCommissionResponse> toResponseList(List<InscriptionToCommissionEntity> inscriptionToCommissionEntityList);
}
