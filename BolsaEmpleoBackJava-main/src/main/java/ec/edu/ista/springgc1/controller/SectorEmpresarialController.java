package ec.edu.ista.springgc1.controller;


import ec.edu.ista.springgc1.exception.AppException;
import ec.edu.ista.springgc1.model.entity.Rol;
import ec.edu.ista.springgc1.model.entity.SectorEmpresarial;
import ec.edu.ista.springgc1.service.impl.SectorEmpresarialServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/sectoresEmpresariales")
public class SectorEmpresarialController {

    @Autowired
    private SectorEmpresarialServiceImpl empresarialService;

    @GetMapping
    ResponseEntity<List<?>> list() {
        return ResponseEntity.ok(empresarialService.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(empresarialService.findById(id));
    }

    @PostMapping
    ResponseEntity<?> create(@Valid @RequestBody SectorEmpresarial sectorEmpresarial) {

        if (empresarialService.findByNombre(sectorEmpresarial.getNombre()).isPresent()){
            throw new AppException(HttpStatus.BAD_REQUEST,"Ya se encuentra registrado el dato ingresado");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(empresarialService.save(sectorEmpresarial));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody SectorEmpresarial sectorEmpresarial) {
        SectorEmpresarial sectorFromDb = empresarialService.findById(id);
        if (!sectorEmpresarial.getNombre().equalsIgnoreCase(sectorFromDb.getNombre()) && empresarialService.findByNombre(sectorEmpresarial.getNombre()).isPresent()){
            throw new AppException(HttpStatus.BAD_REQUEST,"Ya se encuentra registrado el dato ingresado");
        }


        sectorFromDb.setNombre(sectorEmpresarial.getNombre());
        sectorFromDb.setDescripcion(sectorEmpresarial.getDescripcion());

        return ResponseEntity.status(HttpStatus.CREATED).body(empresarialService.save(sectorFromDb));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        SectorEmpresarial sectorFromDb = empresarialService.findById(id);
        empresarialService.delete(sectorFromDb.getId());
        return ResponseEntity.noContent().build();
    }



}
