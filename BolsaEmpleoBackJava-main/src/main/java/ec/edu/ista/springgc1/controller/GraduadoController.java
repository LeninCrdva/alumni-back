package ec.edu.ista.springgc1.controller;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ista.springgc1.model.dto.GraduadoDTO;
import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.service.impl.GraduadoServiceImpl;

@CrossOrigin
@RestController
@RequestMapping("/graduados")
public class GraduadoController {

	@Autowired
    private GraduadoServiceImpl estudianteService;

    @GetMapping
    ResponseEntity<List<?>> list() {
        return ResponseEntity.ok(estudianteService.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.findById(id));
    }

    @GetMapping("/resumen/{id}")
    ResponseEntity<?> findByIdResumen(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.findByIdToDTO(id));
    }


    @GetMapping("/usuario/{id}")
    ResponseEntity<?> findByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.findByUsuario(id));
    }

    @GetMapping("/total")
    ResponseEntity<?> countEstudiantes() {
        return ResponseEntity.ok(Collections.singletonMap("total:", estudianteService.count()));
    }
    @PostMapping
    ResponseEntity<?> create(@Valid @RequestBody GraduadoDTO estudianteDTO) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(estudianteService.save(estudianteDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody GraduadoDTO estudianteDTO) {
    	return ResponseEntity.status(HttpStatus.NO_CONTENT).body(estudianteService.update(id, estudianteDTO));
    }
    
    @PutMapping("postulaciones/{id}")
    public ResponseEntity<?> savePostulaciones(@PathVariable Long id, @RequestBody GraduadoDTO estudianteDTO) {
    	GraduadoDTO gradDTO = estudianteService.findByIdToDTO(id);
    	
    	gradDTO.setIdOferta(estudianteDTO.getIdOferta());
    	
    	return ResponseEntity.status(HttpStatus.NO_CONTENT).body(estudianteService.save(gradDTO));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Graduado estudianteFromDb = estudianteService.findById(id);
        estudianteService.delete(estudianteFromDb.getId());
        return ResponseEntity.noContent().build();
    }

}
