package ec.edu.ista.springgc1.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.fasterxml.jackson.annotation.JsonView;

import ec.edu.ista.springgc1.model.dto.GraduadoDTO;
import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.model.entity.OfertasLaborales;
import ec.edu.ista.springgc1.service.impl.GraduadoServiceImpl;
import ec.edu.ista.springgc1.view.View;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/graduados")
public class GraduadoController {

	@Autowired
    private GraduadoServiceImpl estudianteService;

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping
    ResponseEntity<List<?>> list() {
        return ResponseEntity.ok(estudianteService.findAll());
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.findById(id));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/resumen/{id}")
    ResponseEntity<?> findByIdResumen(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.findByIdToDTO(id));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/usuario/{id}")
    ResponseEntity<?> findByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.findByUsuario(id));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/user/{username}")
    @JsonView(View.City.class) 
    ResponseEntity<List<OfertasLaborales>> findByUserName(@PathVariable("username") String username){
        return ResponseEntity.ok(estudianteService.findByUsuarioNombreUsuario(username));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/total")
    ResponseEntity<?> countEstudiantes() {
        return ResponseEntity.ok(Collections.singletonMap("total:", estudianteService.count()));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GRADUADO')")
    @PostMapping
    ResponseEntity<?> create(@Valid @RequestBody GraduadoDTO estudianteDTO) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(estudianteService.save(estudianteDTO));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GRADUADO')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody GraduadoDTO estudianteDTO) {
    	return ResponseEntity.status(HttpStatus.NO_CONTENT).body(estudianteService.update(id, estudianteDTO));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GRADUADO')")
    @PutMapping("postulaciones/{id}")
    public ResponseEntity<?> savePostulaciones(@PathVariable Long id, @RequestBody GraduadoDTO estudianteDTO) {
        GraduadoDTO gradDTO = estudianteService.findByIdToDTO(id);

        Set<Long> existingIds = new HashSet<>(gradDTO.getIdOferta());
        
        existingIds.addAll(estudianteDTO.getIdOferta());

        gradDTO.setIdOferta(new ArrayList<>(existingIds));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(estudianteService.save(gradDTO));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GRADUADO')")
    @PutMapping("cancel-postulaciones/{id}")
    public ResponseEntity<?> cancelPostulaciones(@PathVariable Long id, @RequestBody GraduadoDTO estudianteDTO) {
        GraduadoDTO gradDTO = estudianteService.findByIdToDTO(id);

        Set<Long> existingIds = new HashSet<>(gradDTO.getIdOferta());

        existingIds.removeAll(estudianteDTO.getIdOferta());

        gradDTO.setIdOferta(new ArrayList<>(existingIds));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(estudianteService.save(gradDTO));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GRADUADO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Graduado estudianteFromDb = estudianteService.findById(id);
        estudianteService.delete(estudianteFromDb.getId());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/without-oferta")
    ResponseEntity<List<?>> listWithOut() {
        return ResponseEntity.ok(estudianteService.findGRaduadoWithOutOfertas());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EMPRESARIO', 'GRADUADO')")
    @GetMapping("/all")
    ResponseEntity<List<Graduado>> findAllGraduados() {
        return ResponseEntity.ok(estudianteService.findAllGraduados());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/count-sex")
    public ResponseEntity<?> countSex(){
        return ResponseEntity.ok(estudianteService.countBySex());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RESPONSABLE_CARRERA')")
    @GetMapping("/with-oferta")
    ResponseEntity<List<Graduado>> findAllGraduadosConOfertas() {
        return ResponseEntity.ok(estudianteService.findGraduadosWithOfertas());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RESPONSABLE_CARRERA')")
    @GetMapping("/sin-experiencia")
    ResponseEntity<List<Graduado>> findAllGraduadosSinExperiencia() {
        return ResponseEntity.ok(estudianteService.findGraduadosSinExperiencia());
    }
}
