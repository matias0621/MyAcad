package MyAcad.Project.backend.Controller.Programs;


import MyAcad.Project.backend.Enum.ProgramType;
import MyAcad.Project.backend.Exception.CareerAlreadyExistsException;
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
@RequestMapping("/careers")
@AllArgsConstructor
public class CareerController {
    private final ProgramService programService;

    @GetMapping()
    public List<ProgramResponse> listCareers() {
        return programService.findByProgramType(ProgramType.ENGINEERING);
    }
    //Paginacion
    @GetMapping("/paginated")
    public Page<ProgramResponse> listCareerPaginated(@RequestParam(name = "page") int page,
                                                     @RequestParam(name = "size") int size) {
        return programService.listProgramPaginatedByProgramType(page,size, ProgramType.ENGINEERING);
    }

    //Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        Optional<ProgramResponse> career = Optional.ofNullable(programService.findProgramById(id));
        if (career.isPresent()) {
            return ResponseEntity.ok(career.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> addCareer(@RequestBody ProgramsDTO dto) {
        try {
            programService.createEngineering(dto);
            return ResponseEntity.ok().build();
        }catch (CareerAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCareer(@PathVariable(name = "id") Long id){
        programService.logicDeleteProgram(id);
        return ResponseEntity.ok().build();
    }

    // Baja definitiva
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> definitiveDeleteCareer(@PathVariable Long id) {
        programService.deleteProgram(id);
        return ResponseEntity.ok().build();
    }
    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCareer(@PathVariable Long id, @RequestBody ProgramsDTO dto){
        try {
            programService.updateProgram(id, dto);
            return ResponseEntity.ok().build();
        }catch (LegajoAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }


}
