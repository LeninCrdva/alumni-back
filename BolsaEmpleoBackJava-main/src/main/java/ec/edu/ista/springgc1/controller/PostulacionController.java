package ec.edu.ista.springgc1.controller;

import com.fasterxml.jackson.annotation.JsonView;
import ec.edu.ista.springgc1.model.dto.PostulacionDto;
import ec.edu.ista.springgc1.model.entity.Postulacion;
import ec.edu.ista.springgc1.service.impl.PostulacionServiceImpl;
import ec.edu.ista.springgc1.view.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/postulaciones")
public class PostulacionController {

    @Autowired
    private PostulacionServiceImpl postulacionService;

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllPostulaciones() {
        return ResponseEntity.ok(postulacionService.findAll());
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/all-postulations-by-graduado/{id}")
    public ResponseEntity<?> getAllPostulacioneByGraduadoId(@PathVariable Long id) {
        return ResponseEntity.ok(postulacionService.findOfertasLaboralesByGraduadoId(id));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(postulacionService.findById(id));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/all-by-oferta-laboral/{id}")
    public ResponseEntity<?> getAllPostulacionesByOfertaLaboralId(@PathVariable Long id) {
        return ResponseEntity.ok(postulacionService.findGraduadosByOfertaLaboralId(id));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/all-sin-postulacion")
    public ResponseEntity<?> getAllGraduadosSinPostulacion() {
        return ResponseEntity.ok(postulacionService.findGraduadosSinPostulacion());
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @PostMapping("")
    public ResponseEntity<?> createPostulacion(@RequestBody PostulacionDto postulacionDto) {
        return ResponseEntity.ok(postulacionService.savePostulacion(postulacionDto));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePostulacion(@PathVariable Long id, @RequestBody PostulacionDto postulacionDto) {
        Postulacion postulacionFromDb = postulacionService.findById(id);
        Postulacion postulacionFromService = postulacionService.mapToEntity(postulacionDto);

        postulacionFromDb.setGraduado(postulacionFromService.getGraduado());
        postulacionFromDb.setOfertaLaboral(postulacionFromService.getOfertaLaboral());
        postulacionFromDb.setEstado(postulacionDto.getEstado());

        return ResponseEntity.ok(postulacionService.save(postulacionFromDb));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @PutMapping("/update-estado/{id}")
    public ResponseEntity<?> updateEstado(@PathVariable Long id, @RequestBody PostulacionDto postulacionDto) {
        return ResponseEntity.ok(postulacionService.updateEstado(id, postulacionDto));
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePostulacion(@PathVariable Long id) {
        postulacionService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/count")
    public ResponseEntity<?> countPostulaciones() {
        return ResponseEntity.ok(postulacionService.count());
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/count-by-date/{date}")
    public ResponseEntity<?> countPostulacionesByFechaPostulacion(@PathVariable String date) {
        return ResponseEntity.ok(postulacionService.countPostulacionByFechaPostulacionIsStartingWithOrderBy(LocalDate.parse(date)));
    }
}
