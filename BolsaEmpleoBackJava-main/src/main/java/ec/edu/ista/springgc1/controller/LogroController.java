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

import ec.edu.ista.springgc1.model.dto.LogroDTO;
import ec.edu.ista.springgc1.model.entity.Logro;
import ec.edu.ista.springgc1.service.impl.LogroServiceImpl;

@RestController
@RequestMapping("logro")
@CrossOrigin(origins = "http://localhost:4200")
public class LogroController {
	
	@Autowired
	private LogroServiceImpl logroService;
	
	@GetMapping("/get-list-logro")
	public ResponseEntity<List<?>> listAllLogro(){
		return ResponseEntity.ok(logroService.findAllDTO());
	}
	
	@GetMapping("/get-logro/{id}")
	public ResponseEntity<?> finLogroById(@PathVariable("id") Long id){
		return ResponseEntity.ok(logroService.findLogroByIdToDTO(id));
	}
	
	@PostMapping("/save-logro")
	public ResponseEntity<?> saveLogro(@Valid @RequestBody LogroDTO logroDTO){
		if(logroDTO.getTipoLogro().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(logroService.save(logroDTO));
	}
	
	@PutMapping("/update-logro/{id}")
	public ResponseEntity<?> updateLogro(@PathVariable("id") Long id,
			@Valid @RequestBody LogroDTO logroDTO){
		LogroDTO logroDB = logroService.findLogroByIdToDTO(id);
		
		logroDB.setDescripcion(logroDTO.getDescripcion());
		logroDB.setTipoLogro(logroDTO.getTipoLogro());
		logroDB.setCedula(logroDTO.getCedula());
	
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(logroService.save(logroDB));
	}
	
	@DeleteMapping("/delete-logro/{id}")
	public ResponseEntity<?> deleteLogro(@PathVariable("id") Long id, 
			@Valid @RequestBody Logro logro){
		LogroDTO logroDB = logroService.findLogroByIdToDTO(id);
		logroService.delete(logroDB.getId());
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
