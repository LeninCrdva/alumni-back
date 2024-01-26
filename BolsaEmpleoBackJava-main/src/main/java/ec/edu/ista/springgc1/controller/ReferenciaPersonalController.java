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
import ec.edu.ista.springgc1.model.dto.ReferenciaPersonalDTO;
import ec.edu.ista.springgc1.model.entity.Referencia_Personal;
import ec.edu.ista.springgc1.service.impl.ReferenciaPersonalServiceImp;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/referencias-personales")
public class ReferenciaPersonalController {

	@Autowired
	private ReferenciaPersonalServiceImp referenciaPersonalService;

	@GetMapping
	ResponseEntity<List<?>> list() {
		return ResponseEntity.ok(referenciaPersonalService.findAll());
	}

	@GetMapping("/{id}")
	ResponseEntity<?> findById(@PathVariable Long id) {
		return ResponseEntity.ok(referenciaPersonalService.findById(id));
	}

	@PostMapping("/create")
	ResponseEntity<?> create(@Valid @RequestBody ReferenciaPersonalDTO referenciaPersonalDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(referenciaPersonalService.save(referenciaPersonalDTO));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Long id,
			@Valid @RequestBody ReferenciaPersonalDTO referenciaPersonalDTO) {
		ReferenciaPersonalDTO currentReference = referenciaPersonalService.findByIdToDTO(id);
		currentReference.setNombreReferencia(referenciaPersonalDTO.getNombreReferencia());
		currentReference.setEmail(referenciaPersonalDTO.getEmail());
		currentReference.setTelefono(referenciaPersonalDTO.getTelefono());

		return ResponseEntity.status(HttpStatus.OK).body(referenciaPersonalService.save(currentReference));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Referencia_Personal referenciaPersonal = referenciaPersonalService.findById(id);
		referenciaPersonalService.delete(referenciaPersonal.getId());
		return ResponseEntity.noContent().build();
	}

}
