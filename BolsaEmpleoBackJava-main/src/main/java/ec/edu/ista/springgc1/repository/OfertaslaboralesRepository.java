package ec.edu.ista.springgc1.repository;

import java.util.List;
import ec.edu.ista.springgc1.model.entity.OfertasLaborales;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;

public interface OfertaslaboralesRepository extends GenericRepository<OfertasLaborales>{
	List<OfertasLaborales> findOfertasByIdIn(List<Long> idOfertas);
}
