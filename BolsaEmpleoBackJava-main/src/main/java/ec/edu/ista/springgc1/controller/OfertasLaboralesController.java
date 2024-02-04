package ec.edu.ista.springgc1.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.sns.model.ResourceNotFoundException;

import ec.edu.ista.springgc1.model.dto.OfertasLaboralesDTO;
import ec.edu.ista.springgc1.model.entity.Contratacion;
import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.model.entity.OfertasLaborales;
import ec.edu.ista.springgc1.repository.ContratacionRepository;
import ec.edu.ista.springgc1.service.impl.GraduadoServiceImpl;
import ec.edu.ista.springgc1.service.impl.OfertaslaboralesServiceImpl;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/ofertas-laborales")
public class OfertasLaboralesController {
	@Autowired
	ContratacionRepository contratacionRepository;
	@Autowired
	private OfertaslaboralesServiceImpl ofertasLaboralesService;
	@Autowired
    private GraduadoServiceImpl  graduadoService;
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
	@GetMapping("/cargos-con-ofertas")
	ResponseEntity<Map<String, Long>> getCargosConOfertas() {
		List<OfertasLaboralesDTO> ofertasLaboralesDTOList = ofertasLaboralesService.findAll();
		Map<String, Long> cargosConOfertas = new HashMap<>();

		for (OfertasLaboralesDTO ofertaLaboralDTO : ofertasLaboralesDTOList) {
			String cargo = ofertaLaboralDTO.getCargo().toLowerCase().replace(" ", ""); // Normaliza el cargo
			cargosConOfertas.merge(cargo, 1L, Long::sum);
		}

		return ResponseEntity.ok(cargosConOfertas);
	}
	  @GetMapping("/graduados/{ofertaId}")
	    ResponseEntity<List<Graduado>> findGraduadosByOfertaId(@PathVariable Long ofertaId) {
	        return ResponseEntity.ok(ofertasLaboralesService.findGraduadosByOfertaId(ofertaId));
	    }

	    @GetMapping("/empresa/{nombreEmpresa}")
	    ResponseEntity<List<OfertasLaborales>> findOfertasByNombreEmpresa(@PathVariable String nombreEmpresa) {
	        return ResponseEntity.ok(ofertasLaboralesService.findOfertasByNombreEmpresa(nombreEmpresa));
	    }

	    @PostMapping("/seleccionar-contratados/{ofertaId}")
	    public ResponseEntity<Contratacion> seleccionarContratados(@PathVariable Long ofertaId, @RequestBody List<Long> graduadosIds) {
	        try {
	            Contratacion contratacion = ofertasLaboralesService.seleccionarContratados(ofertaId, graduadosIds);
	            return ResponseEntity.status(HttpStatus.CREATED).body(contratacion);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }
	    @GetMapping("/contrataciones")
	    public ResponseEntity<List<Contratacion>> getAllContrataciones() {
	        List<Contratacion> contrataciones =contratacionRepository.findAll();
	        return ResponseEntity.ok(contrataciones);
	    }
	    @GetMapping("/contrataciones/{id}")
	    public ResponseEntity<Contratacion> getContratacionById(@PathVariable Long id) {
	        Contratacion contratacion = contratacionRepository.findById(id)
	                .orElseThrow(() -> new ec.edu.ista.springgc1.exception.ResourceNotFoundException("Contratacion", String.valueOf(id)));
	        return ResponseEntity.ok(contratacion);
	    }
	    @DeleteMapping("/contrataciones/{id}")
	    public ResponseEntity<String> deleteContratacionById(@PathVariable Long id) {
	        if (contratacionRepository.existsById(id)) {
	            contratacionRepository.deleteById(id);
	            return ResponseEntity.ok("Contratacion eliminada exitosamente.");
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contratacion no encontrada con ID: " + id);
	        }
	    }







	  


}
