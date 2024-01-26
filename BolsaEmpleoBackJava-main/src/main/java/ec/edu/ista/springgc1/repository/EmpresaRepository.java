package ec.edu.ista.springgc1.repository;


import java.util.Optional;

import ec.edu.ista.springgc1.model.entity.Empresa;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;

public interface EmpresaRepository extends GenericRepository<Empresa>{
	Optional<Empresa> findByNombre(String nombre);
	 public Boolean existsBynombre(String name);
}
