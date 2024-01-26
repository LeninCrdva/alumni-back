package ec.edu.ista.springgc1.repository;

import java.util.Optional;

import ec.edu.ista.springgc1.model.entity.Evento;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;


public interface EventoRepository extends GenericRepository<Evento> {

	Optional<Evento> findById(Long id);
	Optional<Evento> findByNombreEvento(String nombreEvento);
	
}
