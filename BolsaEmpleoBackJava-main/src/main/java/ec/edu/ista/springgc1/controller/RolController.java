package ec.edu.ista.springgc1.controller;

import ec.edu.ista.springgc1.exception.AppException;
import ec.edu.ista.springgc1.model.entity.Rol;
import ec.edu.ista.springgc1.service.impl.RolServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/roles")
public class RolController {

    @Autowired
    private RolServiceImpl rolService;

    @GetMapping
    ResponseEntity<List<?>> list() {
        return ResponseEntity.ok(rolService.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.findById(id));
    }

    @PostMapping
    ResponseEntity<?> create(@Valid @RequestBody Rol rol) {
        if (rolService.findByNombre(rol.getNombre()).isPresent()){
            throw new AppException(HttpStatus.BAD_REQUEST,"Ya se encuentra registrado el Rol");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rolService.save(rol));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Rol rol) {
        Rol rolFromDb = rolService.findById(id);
        if (!rol.getNombre().equalsIgnoreCase(rolFromDb.getNombre()) && rolService.findByNombre(rol.getNombre()).isPresent()){
            throw new AppException(HttpStatus.BAD_REQUEST,"Ya se encuentra registrado el Rol");
        }
        rolFromDb.setNombre(rol.getNombre());
        rolFromDb.setDescripcion(rol.getDescripcion());

        return ResponseEntity.status(HttpStatus.CREATED).body(rolService.save(rolFromDb));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Rol rolFromDb = rolService.findById(id);
        rolService.delete(rolFromDb.getId());
        return ResponseEntity.noContent().build();
    }

}
