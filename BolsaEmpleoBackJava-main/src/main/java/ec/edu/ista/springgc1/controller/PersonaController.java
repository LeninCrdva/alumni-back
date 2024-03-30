package ec.edu.ista.springgc1.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ista.springgc1.exception.AppException;
import ec.edu.ista.springgc1.model.entity.Persona;
import ec.edu.ista.springgc1.service.impl.PersonaServiceImp;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/personas")
public class PersonaController {
    @Autowired
    private PersonaServiceImp personaService;

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping
    ResponseEntity<List<?>> list() {
        return ResponseEntity.ok(personaService.findAll());
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.findById(id));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("cedula/{cedula}")
    ResponseEntity<?> findByCedula(@PathVariable String cedula) {
        return ResponseEntity.ok(personaService.findBycedula(cedula));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'EMPRESARIO', 'ADMINISTRADOR')")
    @PostMapping
    ResponseEntity<?> create(@Valid @RequestBody Persona p) {
        if (p.getFechaNacimiento() == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "La fecha de nacimiento no puede ser nula");
        }

        if (personaService.findBycedula(p.getCedula()).isPresent()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Ya se encuentra registrado esta cedula");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(personaService.save(p));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'EMPRESARIO', 'ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Persona p) {
        Persona pFromDb = personaService.findById(id);
        if (!p.getCedula().equalsIgnoreCase(pFromDb.getCedula()) && personaService.findBycedula(p.getCedula()).isPresent()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Ya se encuentra registrado la cedula, no es posible actualizar");
        }
        pFromDb.setCedula(p.getCedula());
        pFromDb.setApellidoMaterno(p.getApellidoMaterno());
        pFromDb.setApellidoPaterno(p.getApellidoPaterno());
        pFromDb.setFechaNacimiento(p.getFechaNacimiento());
        pFromDb.setPrimerNombre(p.getPrimerNombre());
        pFromDb.setSegundoNombre(p.getSegundoNombre());
        pFromDb.setTelefono(p.getTelefono());

        return ResponseEntity.status(HttpStatus.CREATED).body(personaService.save(pFromDb));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Persona pFromDb = personaService.findById(id);
        personaService.delete(pFromDb.getId());
        return ResponseEntity.noContent().build();
    }
}
