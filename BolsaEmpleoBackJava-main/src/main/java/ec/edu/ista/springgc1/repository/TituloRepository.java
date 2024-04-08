package ec.edu.ista.springgc1.repository;

import ec.edu.ista.springgc1.model.entity.Titulo;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;

import java.util.List;

public interface TituloRepository extends GenericRepository<Titulo>{
    List<Titulo> findAllByGraduadoId(Long id);
}
