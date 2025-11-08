package MyAcad.Project.backend.Service.Programs;

import MyAcad.Project.backend.Exception.CareerAlreadyExistsException;
import MyAcad.Project.backend.Mapper.TechnicalMapper;
import MyAcad.Project.backend.Model.Programs.Technical;
import MyAcad.Project.backend.Model.Programs.TechnicalResponse;
import MyAcad.Project.backend.Repository.Programs.TechnicalRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TechnicalService {
    private final TechnicalRepository repository;
    private final TechnicalMapper mapper;

    public void add(Technical c) {
        if (repository.findTechnicalByName(c.getName()).isPresent()) {
            throw new RuntimeException("Career name already exists");
        }
        repository.save(c);
    }

    public Page<Technical> listTechnicalPaginated(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public List<TechnicalResponse> getByNameIgnoringCase(String name) {
        return mapper.toResponseList(repository.findByNameContainingIgnoreCase(name));
    }

    public ResponseEntity<Void> delete(Long id) {
        Optional<Technical> optionalTechnical = repository.findById(id);
        if (optionalTechnical.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Technical technical = optionalTechnical.get();
        technical.setActive(false);
        repository.save(technical);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> definitiveDeleteTechnical(Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    public List<TechnicalResponse> list() {
        return mapper.toResponseList(repository.findAll());
    }

    public void modify(Long id, Technical c) {
        Optional<Technical> optionalOld = repository.findById(id);
        if (optionalOld.isEmpty()) {
            throw new RuntimeException("Career not found");
        }
        Technical old = optionalOld.get();
        if (!old.getName().equals(c.getName())) {
            if (repository.findTechnicalByName(c.getName()).isPresent()) {
                throw new CareerAlreadyExistsException();
            }
            old.setName(c.getName());
        }
        old.setDescription(c.getDescription());
        old.setDurationMonths(c.getDurationMonths());
        old.setMonthlyFee(c.getMonthlyFee());
        old.setAnnualFee(c.getAnnualFee());
        old.setActive(c.getActive());

        repository.save(old);
    }

    public Optional<TechnicalResponse> getById(Long id){
        return repository.findById(id).map(mapper::toResponse);
    }


}
