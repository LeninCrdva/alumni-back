package ec.edu.ista.springgc1.controller;

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

import ec.edu.ista.springgc1.model.dto.OfertasLaboralesDTO;
import ec.edu.ista.springgc1.model.entity.OfertasLaborales;
import ec.edu.ista.springgc1.service.impl.OfertaslaboralesServiceImpl;

@CrossOrigin
@RestController
@RequestMapping("/ofertas-laborales")
public class OfertasLaboralesController {
	 @Autowired
	    private OfertaslaboralesServiceImpl ofertasLaboralesService;

	    @GetMapping
	    ResponseEntity<List<OfertasLaboralesDTO>> list() {
	        return ResponseEntity.ok(ofertasLaboralesService.findAll());
	    }

	    @GetMapping("/{id}")
	    ResponseEntity<OfertasLaboralesDTO> findById(@PathVariable Long id) {
	        return ResponseEntity.ok(ofertasLaboralesService.findByIdToDTO(id));
	    }

	    @PostMapping
	    ResponseEntity<OfertasLaborales> create(@Valid @RequestBody OfertasLaboralesDTO ofertaLaboralDTO) {
	        return ResponseEntity.status(HttpStatus.CREATED).body(ofertasLaboralesService.save(ofertaLaboralDTO));
	    }

	    @PutMapping("/{id}")
	    ResponseEntity<OfertasLaboralesDTO> update(@PathVariable Long id, @Valid @RequestBody OfertasLaboralesDTO ofertaLaboralDTO) {
	        return ResponseEntity.ok(ofertasLaboralesService.update(id, ofertaLaboralDTO));
	    }
	    
	    @PutMapping("postulado/{id}")
	    ResponseEntity<?> savePostulado(@PathVariable Long id, @RequestBody OfertasLaboralesDTO ofertaLaboralDTO){
	    	OfertasLaboralesDTO ofer = ofertasLaboralesService.findByIdToDTO(id);
	    	
	    	ofer.setCorreoGraduado(ofertaLaboralDTO.getCorreoGraduado());
	    	
	    	return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ofertasLaboralesService.save(ofer));
	    }

	    @DeleteMapping("/{id}")
	    ResponseEntity<?> delete(@PathVariable Long id) {
	        ofertasLaboralesService.delete(id);
	        return ResponseEntity.noContent().build();
	    }
}
