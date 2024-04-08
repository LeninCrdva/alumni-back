package ec.edu.ista.springgc1.controller;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonView;
import ec.edu.ista.springgc1.view.View;
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

import ec.edu.ista.springgc1.model.dto.TituloDTO;
import ec.edu.ista.springgc1.model.entity.Titulo;
import ec.edu.ista.springgc1.service.impl.TituloServiceImpl;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/titulos")
public class TituloController {

	@Autowired
    private TituloServiceImpl tituloService;

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping
    ResponseEntity<List<?>> list() {
        return ResponseEntity.ok(tituloService.findAll());
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/{id}")
    @JsonView(View.Public.class)
    ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tituloService.findById(id));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/resumen/{id}")
    ResponseEntity<?> findByIdResumen(@PathVariable Long id) {
        return ResponseEntity.ok(tituloService.findByIdToDTO(id));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/total")
    ResponseEntity<?> countEstudiantes() {
        return ResponseEntity.ok(Collections.singletonMap("total:", tituloService.count()));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'ADMINISTRADOR')")
    @PostMapping
    ResponseEntity<?> create(@Valid @RequestBody TituloDTO e) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tituloService.save(e));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody TituloDTO e) {
        TituloDTO tituloFromDb = tituloService.findByIdToDTO(id);

        tituloFromDb.setFechaEmision(e.getFechaEmision());
        tituloFromDb.setFechaRegistro(e.getFechaRegistro());
        tituloFromDb.setIdGraduado(e.getIdGraduado());
        tituloFromDb.setInstitucion(e.getInstitucion());
        tituloFromDb.setNivel(e.getNivel());
        tituloFromDb.setNombreTitulo(e.getNombreTitulo());
        tituloFromDb.setNombreCarrera(e.getNombreCarrera());
        tituloFromDb.setNumRegistro(e.getNumRegistro());
        tituloFromDb.setTipo(e.getTipo());

        // Utiliza el método update del servicio para actualizar la instancia existente
        Titulo updatedTitulo = tituloService.update(id, tituloFromDb);

        return ResponseEntity.status(HttpStatus.OK).body(updatedTitulo);
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
    	Titulo tituloFromDb = tituloService.findById(id);
       
    	tituloService.delete(tituloFromDb.getId());
        return ResponseEntity.noContent().build();
    }
}
