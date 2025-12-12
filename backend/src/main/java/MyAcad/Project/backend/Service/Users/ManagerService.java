package MyAcad.Project.backend.Service.Users;

import MyAcad.Project.backend.Configuration.SecurityConfig;
import MyAcad.Project.backend.Enum.Role;
import MyAcad.Project.backend.Exception.DniAlreadyExistsException;
import MyAcad.Project.backend.Exception.EmailAlreadyExistsException;
import MyAcad.Project.backend.Exception.LegajoAlreadyExistsException;
import MyAcad.Project.backend.Model.Users.Manager;
import MyAcad.Project.backend.Model.Users.ManagerCsvDto;
import MyAcad.Project.backend.Repository.Users.ManagerRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ManagerService {
    private final ManagerRepository repository;
    private final UserLookupService userLookupService;
    private PasswordEncoder passwordEncoder;

    public void add(Manager t) {
        if (userLookupService.findByLegajo(t.getLegajo()).isPresent()) {
            throw new LegajoAlreadyExistsException();
        }else if(userLookupService.findByEmail(t.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }else if(userLookupService.findByDni(t.getDni()).isPresent()) {
            throw new DniAlreadyExistsException();
        }

        t.setPassword(SecurityConfig.passwordEncoder().encode(t.getPassword()));

        t = repository.save(t);
        t.setLegajo(String.valueOf(t.getId() + 900000));

        repository.save(t);
    }

    public List<ManagerCsvDto> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<ManagerCsvDto> csvToBean = new CsvToBeanBuilder<ManagerCsvDto>(reader)
                    .withType(ManagerCsvDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        }
    }

    public void saveStudentByCsv(List<ManagerCsvDto> records){
        for (ManagerCsvDto record : records) {
            Manager manager = new Manager();
            manager.setEmail(record.getEmail());
            manager.setPassword(String.valueOf(record.getDni()));
            manager.setActive(true);
            manager.setDni(Integer.parseInt((record.getDni())));
            manager.setName(record.getName());
            manager.setLastName(record.getLastname());
            manager.setRole(Role.MANAGER);
            add(manager);
        }
    }

    public Page<Manager> listManagersPaginated(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public List<Manager> getByLegajoContaining(String legajo) {
        return repository.findByLegajoContaining(legajo);
    }

    public List<Manager> getByFullName(String fullName) {
        return repository.findByFullName(fullName);
    }
    
    public ResponseEntity<Void> delete(Long id){
        if (id == 1){
            throw new RuntimeException("No se puede eliminar este manager");
        }
        Optional<Manager> optionalManager = repository.findById(id);
        if (optionalManager.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Manager manager = optionalManager.get();
        manager.setActive(false);
        repository.save(manager);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> definitiveDeleteManager(Long managerId) {
        if (!repository.existsById(managerId)) {
            return ResponseEntity.notFound().build();
        }

        if (managerId == 1){
            throw new RuntimeException("No se puede eliminar este manager");
        }

        repository.deleteById(managerId);
        return ResponseEntity.ok().build();
    }
    
    public List<Manager> list() {
        return repository.findAll();
    }

    public void modify (Long id, Manager t){
        Manager old = repository.findById(id).get();
        //Verificar si cambió el legajo o el email
        if (!old.getEmail().equals(t.getEmail())) {
            //Verificar si el email nuevo ya se encuentra en uso
            if (userLookupService.findByEmail(t.getEmail()).isPresent()) {
                throw new EmailAlreadyExistsException();
            }
        }
        old.setName(t.getName());
        old.setLastName(t.getLastName());
        old.setEmail(t.getEmail());
        old.setDni(t.getDni());
        old.setActive(t.isActive());

        // Verificar si se ingresó una contraseña nueva, si el usuario no quiso cambiarla debe dejar ese input vacío.
        if (t.getPassword() != null && !t.getPassword().isBlank()) {
            String encoded = passwordEncoder.encode(t.getPassword());
            old.setPassword(encoded);
        }
        repository.save(old);
    }

    public Optional<Manager> getById(Long id){
        return repository.findById(id);
    }

    public Optional<Manager> getByLegajo(String legajo) {
        return repository.findByLegajo(legajo);
    }

    public Optional<Manager> getByEmail(String email){
        return repository.findByEmail(email);
    }
}
