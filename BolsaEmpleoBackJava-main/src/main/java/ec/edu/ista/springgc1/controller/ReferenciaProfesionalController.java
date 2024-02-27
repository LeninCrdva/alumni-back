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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ec.edu.ista.springgc1.model.dto.ReferenciaProfesionalDTO;
import ec.edu.ista.springgc1.model.entity.ReferenciaProfesional;
import ec.edu.ista.springgc1.service.impl.ReferenciaProfesionalServiceImp;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/referencias-profesionales")
public class ReferenciaProfesionalController {

	@Autowired
	private ReferenciaProfesionalServiceImp referenciaProfesionalService;

	@GetMapping
	ResponseEntity<List<?>> list() {
		return ResponseEntity.ok(referenciaProfesionalService.findAll());
	}

	@GetMapping("/{id}")
	ResponseEntity<?> findById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(referenciaProfesionalService.findById(id));
	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody ReferenciaProfesionalDTO referenciaProfesionalDTO) {
		// return ResponseEntity.status(HttpStatus.CREATED)
		// .body(referenciaProfesionalService.save(referenciaProfesionalDTO));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(referenciaProfesionalService.save(referenciaProfesionalDTO));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Long id,
			@Valid @RequestBody ReferenciaProfesionalDTO referenciaProfesionalDTO) {

		ReferenciaProfesionalDTO currentProfesionalReference = referenciaProfesionalService.findByIdToDTO(id);

		currentProfesionalReference.setNombre(referenciaProfesionalDTO.getNombre());
		currentProfesionalReference.setInstitucion(referenciaProfesionalDTO.getInstitucion());
		currentProfesionalReference.setEmail(referenciaProfesionalDTO.getEmail());

		return ResponseEntity.status(HttpStatus.OK)
				.body(referenciaProfesionalService.save(currentProfesionalReference));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		ReferenciaProfesional referenciaProfesional = referenciaProfesionalService.findById(id);
		referenciaProfesionalService.delete(referenciaProfesional.getId());
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/usuario/{nombreUsuario}")
	ResponseEntity<List<ReferenciaProfesionalDTO>> findByNombreUsuario(
			@PathVariable("nombreUsuario") String nombreUsuario) {
		List<ReferenciaProfesionalDTO> referencias = referenciaProfesionalService.findByNombreUsuario(nombreUsuario);
		return ResponseEntity.ok(referencias);
	}

}
