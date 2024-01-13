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

import ec.edu.ista.springgc1.model.dto.ExperienciaDTO;
import ec.edu.ista.springgc1.model.entity.Experiencia;
import ec.edu.ista.springgc1.service.impl.ExperienciaServiceImp;


@CrossOrigin
@RestController
@RequestMapping("/experiencias")
public class ExperienciaController {

	@Autowired
	private ExperienciaServiceImp experienciaServiceImp;

	@GetMapping
	ResponseEntity<List<?>> list() {
		return ResponseEntity.ok(experienciaServiceImp.findAll());
	}

	@GetMapping("/{id}")
	ResponseEntity<?> findById(@PathVariable Long id) {
		return ResponseEntity.ok(experienciaServiceImp.findById(id));
	}

	@PostMapping("/create")
	ResponseEntity<?> create(@Valid @RequestBody ExperienciaDTO experienciaDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(experienciaServiceImp.save(experienciaDTO));
	}

	@PutMapping("update/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Long id,
			@Valid @RequestBody ExperienciaDTO experienciaDTO) {
		ExperienciaDTO currentExperiencie = experienciaServiceImp.findByIdToDTO(id);
		currentExperiencie.setInstitucionNombre(experienciaDTO.getInstitucionNombre());
		currentExperiencie.setActividad(experienciaDTO.getActividad());
		currentExperiencie.setCargo(experienciaDTO.getCargo());
		currentExperiencie.setDuracion(experienciaDTO.getDuracion());


		return ResponseEntity.status(HttpStatus.OK).body(experienciaServiceImp.save(currentExperiencie));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Experiencia experiencia = experienciaServiceImp.findById(id);
		experienciaServiceImp.delete(experiencia.getId());
		return ResponseEntity.noContent().build();
	}
	
}
