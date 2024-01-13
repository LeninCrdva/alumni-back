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

import ec.edu.ista.springgc1.model.dto.TituloDTO;
import ec.edu.ista.springgc1.model.entity.Titulo;
import ec.edu.ista.springgc1.service.impl.TituloServiceImpl;

@CrossOrigin
@RestController
@RequestMapping("/titulos")
public class TituloController {
	@Autowired
    private TituloServiceImpl tituloService;

    @GetMapping
    ResponseEntity<List<?>> list() {
        return ResponseEntity.ok(tituloService.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tituloService.findById(id));
    }

    @GetMapping("/resumen/{id}")
    ResponseEntity<?> findByIdResumen(@PathVariable Long id) {
        return ResponseEntity.ok(tituloService.findByIdToDTO(id));
    }



    @GetMapping("/total")
    ResponseEntity<?> countEstudiantes() {
        return ResponseEntity.ok(Collections.singletonMap("total:", tituloService.count()));
    }
    @PostMapping
    ResponseEntity<?> create(@Valid @RequestBody TituloDTO e) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tituloService.save(e));
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody TituloDTO e) {
    	TituloDTO tituloFromDb = tituloService.findByIdToDTO(id);
        
    	tituloFromDb.setFecha_emision(e.getFecha_emision());
    	tituloFromDb.setFecha_registro(e.getFecha_registro());
    	tituloFromDb.setIdgraduado(e.getIdgraduado());
    	tituloFromDb.setInstitucion(e.getInstitucion());
    	tituloFromDb.setNivel(e.getInstitucion());
    	tituloFromDb.setNombre_titulo(e.getNombre_titulo());
    	tituloFromDb.setNombrecarrera(e.getNombrecarrera());
   
    	tituloFromDb.setNum_registro(e.getNum_registro());
    	tituloFromDb.setTipo(e.getTipo());

        return ResponseEntity.status(HttpStatus.CREATED).body(tituloService.save(tituloFromDb));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
    	Titulo tituloFromDb = tituloService.findById(id);
       
    	tituloService.delete(tituloFromDb.getId());
        return ResponseEntity.noContent().build();
    }


}
