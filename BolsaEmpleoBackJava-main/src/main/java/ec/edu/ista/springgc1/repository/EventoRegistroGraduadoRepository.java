package ec.edu.ista.springgc1.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import ec.edu.ista.springgc1.model.entity.Evento;
import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.model.entity.Registro_Evento_Grad;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;

@Repository
public interface EventoRegistroGraduadoRepository extends GenericRepository<Registro_Evento_Grad> {

	Optional<Registro_Evento_Grad> findById(Long id);
	
	Boolean existsByEventoAndGraduado(Evento evento, Graduado graduado);
}
