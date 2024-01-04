package ec.edu.ista.springgc1.repository;
import ec.edu.ista.springgc1.model.entity.AreaTrabajo;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AreaTrabajoRepository extends GenericRepository<AreaTrabajo> {

    Optional<AreaTrabajo> findByNombre(String nombre);
}
