package ec.edu.ista.springgc1.controller;

import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonView;
import ec.edu.ista.springgc1.view.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/ofertas-laborales")
public class OfertasLaboralesController {

    @Autowired
    ContratacionRepository contratacionRepository;

    @Autowired
    private OfertaslaboralesServiceImpl ofertasLaboralesService;

    @Autowired
    private GraduadoServiceImpl graduadoService;

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping
    @JsonView(View.Public.class)
    ResponseEntity<List<OfertasLaboralesDTO>> list() {
        return ResponseEntity.ok(ofertasLaboralesService.findAll());
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/ofertas-sin-postular/{id}")
    @JsonView(View.Public.class)
    public ResponseEntity<?> getOfertasSinPostularPorGraduado(@PathVariable Long id) {
        List<OfertasLaboralesDTO> ofertasSinPostularPorGraduado = ofertasLaboralesService.getOfertasSinPostulacion(id);

        return ResponseEntity.ok(ofertasSinPostularPorGraduado);
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/{id}")
    @JsonView(View.Public.class)
    ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ofertasLaboralesService.findById(id));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("dto/{id}")
    @JsonView(View.Public.class)
    ResponseEntity<OfertasLaboralesDTO> findByIdDTO(@PathVariable Long id) {
        return ResponseEntity.ok(ofertasLaboralesService.findByIdToDTO(id));
    }

    @PreAuthorize("hasAnyRole('EMPRESARIO')")
    @PostMapping
    ResponseEntity<OfertasLaborales> create(@Valid @RequestBody OfertasLaboralesDTO ofertaLaboralDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ofertasLaboralesService.save(ofertaLaboralDTO));
    }

    @PreAuthorize("hasAnyRole('EMPRESARIO', 'ADMINISTRADOR')")
    @PutMapping("/{id}")
    ResponseEntity<OfertasLaboralesDTO> update(@PathVariable Long id,
                                               @Valid @RequestBody OfertasLaboralesDTO ofertaLaboralDTO) {
        return ResponseEntity.ok(ofertasLaboralesService.update(id, ofertaLaboralDTO));
    }

    @PreAuthorize("hasAnyRole('EMPRESARIO', 'ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        ofertasLaboralesService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/usuario/{nombreUsuario}")
    @JsonView(View.Public.class)
    ResponseEntity<List<OfertasLaboralesDTO>> findByNombreUsuario(@PathVariable("nombreUsuario") String nombreUsuario) {
        List<OfertasLaboralesDTO> referencias = ofertasLaboralesService.findByNombreUsuario(nombreUsuario);
        return ResponseEntity.ok(referencias);
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/postulaciones-por-dia")
    @JsonView(View.Public.class)
    public ResponseEntity<Map<String, Long>> calcularPostulacionesPorDia() {
        Map<LocalDate, Long> postulacionesPorDia = ofertasLaboralesService.calcularPostulacionesPorDia();

        // Convertir las claves (LocalDate) a cadenas
        Map<String, Long> postulacionesPorDiaStringKey = new HashMap<>();
        for (Map.Entry<LocalDate, Long> entry : postulacionesPorDia.entrySet()) {
            postulacionesPorDiaStringKey.put(entry.getKey().toString(), entry.getValue());
        }

        return ResponseEntity.ok(postulacionesPorDiaStringKey);
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/empresario/{nombreUsuario}")
    @JsonView(View.Public.class)
    ResponseEntity<List<OfertasLaboralesDTO>> findEmpresarioByNombreUsuario(
            @PathVariable("nombreUsuario") String nombreUsuario) {
        List<OfertasLaboralesDTO> referencias = ofertasLaboralesService.findEmpresarioByNombreUsuario(nombreUsuario);
        return ResponseEntity.ok(referencias);
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/cargos-con-ofertas")
    @JsonView(View.Public.class)
    ResponseEntity<Map<String, Long>> getCargosConOfertas() {
        List<OfertasLaboralesDTO> ofertasLaboralesDTOList = ofertasLaboralesService.findAll();
        Map<String, Long> cargosConOfertas = new HashMap<>();

        for (OfertasLaboralesDTO ofertaLaboralDTO : ofertasLaboralesDTOList) {
            String cargo = ofertaLaboralDTO.getCargo().toLowerCase().trim();
            cargosConOfertas.merge(cargo, 1L, Long::sum);
        }

        return ResponseEntity.ok(cargosConOfertas);
    }

    @PreAuthorize("hasAnyRole('EMPRESARIO', 'RESPONSABLE_CARRERA', 'ADMINISTRADOR')")
    @GetMapping("/graduados/{ofertaId}")
    @JsonView(View.Public.class)
    ResponseEntity<List<Graduado>> findGraduadosByOfertaId(@PathVariable Long ofertaId) {
        return ResponseEntity.ok(ofertasLaboralesService.findGraduadosByOfertaId(ofertaId));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/empresa/{nombreEmpresa}")
    @JsonView(View.Public.class)
    ResponseEntity<List<OfertasLaborales>> findOfertasByNombreEmpresa(@PathVariable String nombreEmpresa) {
        return ResponseEntity.ok(ofertasLaboralesService.findOfertasByNombreEmpresa(nombreEmpresa));
    }

    @PreAuthorize("hasRole('EMPRESARIO')")
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

    @PreAuthorize("hasAnyRole('EMPRESARIO', 'RESPONSABLE_CARRERA','ADMINISTRADOR')")
    @GetMapping("/contrataciones")
    @JsonView(View.Public.class)
    public ResponseEntity<List<Contratacion>> getAllContrataciones() {
        List<Contratacion> contrataciones = contratacionRepository.findAll();
        return ResponseEntity.ok(contrataciones);
    }

    @PreAuthorize("hasAnyRole('EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/contrataciones/{id}")
    @JsonView(View.Public.class)
    public ResponseEntity<Contratacion> getContratacionById(@PathVariable Long id) {
        Contratacion contratacion = contratacionRepository.findById(id)
                .orElseThrow(() -> new ec.edu.ista.springgc1.exception.ResourceNotFoundException("Contratacion", String.valueOf(id)));
        return ResponseEntity.ok(contratacion);
    }

    @PreAuthorize("hasAnyRole('EMPRESARIO', 'RESPONSABLE_CARRERA', 'ADMINISTRADOR')")
    @DeleteMapping("/contrataciones/{id}")
    public ResponseEntity<String> deleteContratacionById(@PathVariable Long id) {
        if (contratacionRepository.existsById(id)) {
            contratacionRepository.deleteById(id);
            return ResponseEntity.ok("Contratacion eliminada exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contratacion no encontrada con ID: " + id);
        }
    }

    @PreAuthorize("hasAnyRole('EMPRESARIO', 'REPONSABLE_CARRERA', 'ADMINISTRADOR')")
    @GetMapping("/ofertaLaboral/{ofertaLaboralId}")
    @JsonView(View.Public.class)
    public ResponseEntity<List<Contratacion>> getContratacionesPorOfertaLaboral(@PathVariable Long ofertaLaboralId) {
        List<Contratacion> contrataciones = ofertasLaboralesService.getContratacionesPorOfertaLaboral(ofertaLaboralId);
        return ResponseEntity.ok(contrataciones);
    }

    
    @GetMapping("/foto-portada/{id}")
    @JsonView(View.Public.class)
    public ResponseEntity<byte[]> getFotoPortadaById(@PathVariable Long id) {
        OfertasLaboralesDTO ofertaLaboralDTO = ofertasLaboralesService.findByIdToDTO(id);
        
        String fotoBase64 = ofertaLaboralDTO.getFotoPortada();
        
        byte[] fotoBytes = Base64.getDecoder().decode(fotoBase64.split(",")[1]); 

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); 

        return new ResponseEntity<>(fotoBytes, headers, HttpStatus.OK);
    }
}
