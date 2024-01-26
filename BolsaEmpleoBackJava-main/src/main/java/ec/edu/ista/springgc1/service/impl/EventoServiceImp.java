package ec.edu.ista.springgc1.service.impl;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ec.edu.ista.springgc1.exception.AppException;
import ec.edu.ista.springgc1.exception.ResourceNotFoundException;
import ec.edu.ista.springgc1.model.dto.EventoDTO;
import ec.edu.ista.springgc1.model.entity.Administrador;
import ec.edu.ista.springgc1.model.entity.Evento;
import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.model.entity.Registro_Evento_Grad;
import ec.edu.ista.springgc1.repository.AdministradorRepository;
import ec.edu.ista.springgc1.repository.EventoRegistroGraduadoRepository;
import ec.edu.ista.springgc1.repository.EventoRepository;
import ec.edu.ista.springgc1.repository.GraduadoRepository;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.map.Mapper;

@Service
public class EventoServiceImp extends GenericServiceImpl<Evento> implements Mapper<Evento, EventoDTO> {

	@Autowired
	private EventoRepository eventoRepository;

	@Autowired
	private AdministradorRepository administradorRepository;

	@Override
	public Evento mapToEntity(EventoDTO eventoDTO) {
		Evento evento = eventoRepository.findByNombreEvento(eventoDTO.getNombreEvento()).orElse(new Evento());

		Administrador administrador = administradorRepository
				.findByUsuarioPersonaCedulaContaining(eventoDTO.getCedula())
				.orElseThrow(() -> new ResourceNotFoundException("De Administrador", eventoDTO.getCedula()));

		evento.setId(eventoDTO.getId());
		evento.setAdmin(administrador);
		evento.setNombreEvento(eventoDTO.getNombreEvento());
		evento.setLugar(eventoDTO.getLugar());
		evento.setHora_inicio(LocalTime.parse(eventoDTO.getHoraInicio()));
		evento.setHora_fin(LocalTime.parse(eventoDTO.getHoraInicio()));
		evento.setFecha(eventoDTO.getFecha());
		evento.setDescripcion(eventoDTO.getDescripcion());

		if (LocalTime.parse(eventoDTO.getHoraInicio()).isAfter(LocalTime.parse(eventoDTO.getHoraFin()))) {
			throw new RuntimeException("La hora de inicio no debe ser menor a la hora de fin");
		}

		if (eventoDTO.getFecha().isBefore(LocalDate.now())) {
			throw new RuntimeException("La fecha debe ser de hoy en adelante");
		}

		return evento;
	}

	@Override
	public EventoDTO mapToDTO(Evento evento) {
		EventoDTO eventoDTO = new EventoDTO();

		eventoDTO.setId(evento.getId());
		eventoDTO.setCedula(evento.getAdmin().getUsuario().getPersona().getCedula());
		eventoDTO.setNombreEvento(evento.getNombreEvento());
		eventoDTO.setFecha(evento.getFecha());
		eventoDTO.setHoraInicio(String.valueOf(evento.getHora_inicio()));
		eventoDTO.setHoraFin(String.valueOf(evento.getHora_inicio()));
		eventoDTO.setLugar(evento.getLugar());
		eventoDTO.setDescripcion(evento.getDescripcion());
		return eventoDTO;
	}

	/*
	 * public Registro_Evento_Grad AsignEventToGraduate(EventoDTO eventoDTO, String
	 * cedula) { Registro_Evento_Grad registroEventoGraduado = new
	 * Registro_Evento_Grad();
	 * 
	 * Evento evento =
	 * eventoRepository.findByNombreEvento(eventoDTO.getNombreEvento())
	 * .orElseThrow(() -> new ResourceNotFoundException("Evento",
	 * eventoDTO.getCedulaAdmin()));
	 * 
	 * Graduado graduado =
	 * graduadoRepository.findByUsuarioPersonaCedulaContaining(cedula)
	 * .orElseThrow(() -> new ResourceNotFoundException("Graduado",
	 * eventoDTO.getCedulaAdmin()));
	 * 
	 * registroEventoGraduado.setEvento(evento);
	 * registroEventoGraduado.setGraduado(graduado);
	 * registroEventoGraduado.setFecha_registro(LocalDate.now()); return
	 * registroEventoGraduado; }
	 */
	@Override
	public List<?> findAll() {
		return eventoRepository.findAll().stream().map(c -> mapToDTO(c)).collect(Collectors.toList());
	}

	public EventoDTO findByIdToDTO(Long id) {
		Evento evento = eventoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id", id));
		return mapToDTO(evento);
	}

	public Administrador findByUsuarioPersonaCedulaContaining(String cedula) {
		return administradorRepository.findByUsuarioPersonaCedulaContaining(cedula)
				.orElseThrow(() -> new ResourceNotFoundException("cedula", cedula));
	}

	@Override
	public Evento save(Object entity) {
		return eventoRepository.save(mapToEntity((EventoDTO) entity));
	}
	/*
	 * public Registro_Evento_Grad save(Registro_Evento_Grad registro_Evento_Grad) {
	 * return eventoRegistroGraduadoRepository.save(registro_Evento_Grad); }
	 */
}
