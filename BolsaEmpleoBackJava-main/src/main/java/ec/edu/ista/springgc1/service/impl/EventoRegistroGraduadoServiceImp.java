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
                .orElseThrow(() -> new ResourceNotFoundException("Evento", eventoDTO.getCedulaAdmin()));

        Graduado graduado = graduadoRepository.findByUsuarioPersonaCedulaContaining(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Graduado", cedula));

        Registro_Evento_Grad registroEventoGraduado = new Registro_Evento_Grad();
        registroEventoGraduado.setEvento(evento);
        registroEventoGraduado.setGraduado(graduado);
        registroEventoGraduado.setFecha_registro(LocalDate.now());

        return eventoRegistroGraduadoRepository.save(registroEventoGraduado);
    }
}
