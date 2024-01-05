package ec.edu.ista.springgc1.controller;

import ec.edu.ista.springgc1.exception.AppException;
import ec.edu.ista.springgc1.model.entity.AreaTrabajo;
import ec.edu.ista.springgc1.service.impl.AreaTrabajoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/areasTrabajo")
public class AreaTrabajoController {

    @Autowired
    private AreaTrabajoServiceImpl areaTrabajoService;

    @GetMapping
    ResponseEntity<List<?>> list() {
        return ResponseEntity.ok(areaTrabajoService.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(areaTrabajoService.findById(id));
    }

    @PostMapping
    ResponseEntity<?> create(@Valid @RequestBody AreaTrabajo areaTrabajo) {

        if (areaTrabajoService.findByNombre(areaTrabajo.getNombre()).isPresent()){
            throw new AppException(HttpStatus.BAD_REQUEST,"El dato ingresado ya se encuentra registrado");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(areaTrabajoService.save(areaTrabajo));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody AreaTrabajo areaTrabajo) {
        AreaTrabajo areaTrabajoFromDb = areaTrabajoService.findById(id);
        if (!areaTrabajo.getNombre().equalsIgnoreCase(areaTrabajoFromDb.getNombre())&& areaTrabajoService.findByNombre(areaTrabajo.getNombre()).isPresent()){
            throw new AppException(HttpStatus.BAD_REQUEST,"El dato ingresado ya se encuentra registrado");
        }

        areaTrabajoFromDb.setNombre(areaTrabajo.getNombre());

        return ResponseEntity.status(HttpStatus.CREATED).body(areaTrabajoService.save(areaTrabajoFromDb));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        AreaTrabajo areaTrabajoFromDb = areaTrabajoService.findById(id);

        areaTrabajoService.delete(areaTrabajoFromDb.getId());
        return ResponseEntity.noContent().build();
    }
}
