package ec.edu.ista.springgc1.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.edu.ista.springgc1.exception.ResourceNotFoundException;
import ec.edu.ista.springgc1.model.dto.EventoDTO;
import ec.edu.ista.springgc1.model.entity.Evento;
import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.model.entity.Registro_Evento_Grad;
import ec.edu.ista.springgc1.repository.EventoRegistroGraduadoRepository;
import ec.edu.ista.springgc1.repository.EventoRepository;
import ec.edu.ista.springgc1.repository.GraduadoRepository;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;

@Service
public class EventoRegistroGraduadoServiceImp extends GenericServiceImpl<Registro_Evento_Grad> {

	@Autowired
	private EventoRepository eventoRepository;

	@Autowired
	private GraduadoRepository graduadoRepository;

	@Autowired
	private EventoRegistroGraduadoRepository eventoRegistroGraduadoRepository;

	@Transactional
	public Registro_Evento_Grad AsignEventToGraduate(EventoDTO eventoDTO, String cedula) {
		Evento evento = eventoRepository.findByNombreEvento(eventoDTO.getNombreEvento())
				.orElseThrow(() -> new ResourceNotFoundException("Evento", eventoDTO.getNombreEvento()));

		Graduado graduado = graduadoRepository.findByUsuarioPersonaCedulaContaining(cedula)
				.orElseThrow(() -> new ResourceNotFoundException("Graduado", cedula));

		if ((eventoRegistroGraduadoRepository.existsByEventoAndGraduado(evento, graduado))) {
			throw new RuntimeException("El evento ya está asignado al graduado.");
		}

		Registro_Evento_Grad registroEventoGraduado = new Registro_Evento_Grad();
		registroEventoGraduado.setEvento(evento);
		registroEventoGraduado.setGraduado(graduado);
		registroEventoGraduado.setFecha_registro(LocalDate.now());

		return eventoRegistroGraduadoRepository.save(registroEventoGraduado);
	}

	@Transactional
	public Registro_Evento_Grad updateEventGraduate(EventoDTO eventoDTO, String cedula, Long id) {
		Evento evento = eventoRepository.findByNombreEvento(eventoDTO.getNombreEvento())
				.orElseThrow(() -> new ResourceNotFoundException("Evento", eventoDTO.getNombreEvento()));
/*
		Graduado graduado = graduadoRepository.findByUsuarioPersonaCedulaContaining(cedula)
				.orElseThrow(() -> new ResourceNotFoundException("Graduado", cedula));*/
/*
		if ((eventoRegistroGraduadoRepository.existsByEventoAndGraduado(evento, graduado))) {
			throw new RuntimeException("El evento ya está asignado al graduado.");
		}*/

		Registro_Evento_Grad registroEventoGraduado = eventoRegistroGraduadoRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("id", id));
		registroEventoGraduado.setEvento(evento);
		//registroEventoGraduado.setGraduado(graduado);
		registroEventoGraduado.setFecha_registro(LocalDate.now());

		return eventoRegistroGraduadoRepository.save(registroEventoGraduado);
	}
	/*
	public void deleteEventGraduate(Long id) {
		eventoRegistroGraduadoRepository.deleteById(id);
	}*/
}
