package ec.edu.ista.springgc1.controller;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ec.edu.ista.springgc1.model.dto.EventoDTO;
import ec.edu.ista.springgc1.model.entity.Evento;
import ec.edu.ista.springgc1.model.entity.Registro_Evento_Grad;
import ec.edu.ista.springgc1.service.impl.EventoRegistroGraduadoServiceImp;
import ec.edu.ista.springgc1.service.impl.EventoServiceImp;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoServiceImp eventoService;

    @Autowired
    private EventoRegistroGraduadoServiceImp eventoRegistroGraduadoServiceImp;

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping
    ResponseEntity<List<?>> list() {
        return ResponseEntity.ok(eventoService.findAll());
    }

    @PreAuthorize("hasAnyRole('GRADUADO', 'RESPONSABLE_CARRERA', 'EMPRESARIO', 'ADMINISTRADOR')")
    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.findById(id));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/create")
    ResponseEntity<?> create(@Valid @RequestBody EventoDTO eventoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.save(eventoDTO));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')") //<-- If you need other role, you can change "hasRole('ADMINISTRADOR')" to "hasAnyRole('ADMINISTRADOR', 'GRADUADO')"
    @PostMapping("/asign-event")
    ResponseEntity<?> asignEvent(@Valid @RequestBody EventoDTO eventoDTO, @RequestParam("cedula") String cedula) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventoRegistroGraduadoServiceImp.AsignEventToGraduate(eventoDTO, cedula));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @PutMapping("/update-asign-event/{id}")
    public ResponseEntity<?> updateAsignEvent(@PathVariable("id") Long id, @Valid @RequestBody EventoDTO eventoDTO,
                                              @RequestParam("cedula") String cedula) {
        // Registro_Evento_Grad currentAsignEvent =
        // eventoRegistroGraduadoServiceImp.findById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventoRegistroGraduadoServiceImp.updateEventGraduate(eventoDTO, cedula, id));
    }

	
	/*
	@DeleteMapping("/asign-event/{id}")
	public ResponseEntity<?> AsignEvent(@PathVariable Long id) {
		Registro_Evento_Grad eventGraduate = eventoRegistroGraduadoServiceImp.findById(id);
		eventoService.delete(eventGraduate.getId());
		return ResponseEntity.noContent().build();
	}*/

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @PutMapping("update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @Valid @RequestBody EventoDTO eventoDTO) {
        EventoDTO currentEvent = eventoService.findByIdToDTO(id);

        currentEvent.setNombreEvento(eventoDTO.getNombreEvento());
        currentEvent.setFecha(eventoDTO.getFecha());
        currentEvent.setHoraInicio(eventoDTO.getHoraInicio());
        currentEvent.setHoraFin(eventoDTO.getHoraFin());
        currentEvent.setLugar(eventoDTO.getLugar());
        currentEvent.setDescripcion(eventoDTO.getDescripcion());
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.save(currentEvent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Evento evento = eventoService.findById(id);
        eventoService.delete(evento.getId());
        return ResponseEntity.noContent().build();
    }

}
