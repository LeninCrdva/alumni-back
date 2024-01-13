package ec.edu.ista.springgc1.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import ec.edu.ista.springgc1.model.entity.ReferenciaProfesional;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;

@Repository
public interface ReferenciaProfesionalRepository extends GenericRepository<ReferenciaProfesional> {

	Optional<ReferenciaProfesional> findById(Long id);
	
}
