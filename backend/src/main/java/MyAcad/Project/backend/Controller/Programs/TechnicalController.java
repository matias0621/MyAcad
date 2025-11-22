package MyAcad.Project.backend.Controller.Programs;

import MyAcad.Project.backend.Enum.ProgramType;
import MyAcad.Project.backend.Exception.LegajoAlreadyExistsException;
import MyAcad.Project.backend.Model.Programs.*;
import MyAcad.Project.backend.Service.Programs.ProgramService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/technicals")
@AllArgsConstructor
public class TechnicalController {
    private final ProgramService programService;

    @GetMapping()
    public List<ProgramResponse> listTechnicals() {
        return programService.findByProgramType(ProgramType.TECHNICAL);
    }

    //Paginacion
    @GetMapping("/paginated")
    public Page<ProgramResponse> listTechnicalPaginated(@RequestParam(name = "page") int page,
                                            @RequestParam(name = "size") int size) {
        return programService.listProgramPaginatedByProgramType(page, size, ProgramType.TECHNICAL);
    }

    //Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        Optional<ProgramResponse> technical = Optional.ofNullable(programService.findProgramById(id));
        if (technical.isPresent()) {
            return ResponseEntity.ok(technical.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> addTechnical(@RequestBody ProgramsDTO dto) {
        try {
            programService.createTechnical(dto);
            return ResponseEntity.ok().build();
        }catch (LegajoAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTechnical(@PathVariable(name = "id") Long id){
        programService.logicDeleteProgram(id);
        return ResponseEntity.ok().build();

    }

    // Baja definitiva
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> definitiveDeleteTechnical(@PathVariable Long id) {
        programService.deleteProgram(id);
        return ResponseEntity.ok().build();
    }
    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTechnical(@PathVariable Long id, @RequestBody ProgramsDTO dto){
        try {
            programService.updateProgram(id, dto);
            return ResponseEntity.ok().build();
        }catch (LegajoAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }
}
