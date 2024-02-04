package ec.edu.ista.springgc1.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@CrossOrigin(origins = "http://localhost:4200")
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
	ResponseEntity<?> findById(@PathVariable Long id) {
		return ResponseEntity.ok(ofertasLaboralesService.findById(id));
	}

	@GetMapping("dto/{id}")
	ResponseEntity<OfertasLaboralesDTO> findByIdDTO(@PathVariable Long id) {
		return ResponseEntity.ok(ofertasLaboralesService.findByIdToDTO(id));
	}

	@PostMapping
	ResponseEntity<OfertasLaborales> create(@Valid @RequestBody OfertasLaboralesDTO ofertaLaboralDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(ofertasLaboralesService.save(ofertaLaboralDTO));
	}

	@PutMapping("/{id}")
	ResponseEntity<OfertasLaboralesDTO> update(@PathVariable Long id,
			@Valid @RequestBody OfertasLaboralesDTO ofertaLaboralDTO) {
		return ResponseEntity.ok(ofertasLaboralesService.update(id, ofertaLaboralDTO));
	}

	@PutMapping("postulado/{id}")
	ResponseEntity<?> savePostulado(@PathVariable Long id, @RequestBody OfertasLaboralesDTO ofertaLaboralDTO) {
		OfertasLaboralesDTO ofer = ofertasLaboralesService.findByIdToDTO(id);

		ofer.setCorreoGraduado(ofertaLaboralDTO.getCorreoGraduado());

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ofertasLaboralesService.update(id, ofer));
	}

	@DeleteMapping("/{id}")
	ResponseEntity<?> delete(@PathVariable Long id) {
		ofertasLaboralesService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/usuario/{nombreUsuario}")
	ResponseEntity<List<OfertasLaboralesDTO>> findByNombreUsuario(@PathVariable("nombreUsuario") String nombreUsuario) {
		List<OfertasLaboralesDTO> referencias = ofertasLaboralesService.findByNombreUsuario(nombreUsuario);
		return ResponseEntity.ok(referencias);
	}

	@GetMapping("/postulaciones-por-dia")
	public ResponseEntity<Map<String, Long>> calcularPostulacionesPorDia() {
		Map<LocalDate, Long> postulacionesPorDia = ofertasLaboralesService.calcularPostulacionesPorDia();

		// Convertir las claves (LocalDate) a cadenas
		Map<String, Long> postulacionesPorDiaStringKey = new HashMap<>();
		for (Map.Entry<LocalDate, Long> entry : postulacionesPorDia.entrySet()) {
			postulacionesPorDiaStringKey.put(entry.getKey().toString(), entry.getValue());
		}

		return ResponseEntity.ok(postulacionesPorDiaStringKey);
	}

	@GetMapping("/empresario/{nombreUsuario}")
	ResponseEntity<List<OfertasLaboralesDTO>> findEmpresarioByNombreUsuario(
			@PathVariable("nombreUsuario") String nombreUsuario) {
		List<OfertasLaboralesDTO> referencias = ofertasLaboralesService.findEmpresarioByNombreUsuario(nombreUsuario);
		return ResponseEntity.ok(referencias);
	}
}
