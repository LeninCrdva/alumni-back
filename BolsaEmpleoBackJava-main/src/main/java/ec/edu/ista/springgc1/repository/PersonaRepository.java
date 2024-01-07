package ec.edu.ista.springgc1.repository;

import ec.edu.ista.springgc1.model.entity.Persona;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;
import java.util.Optional;
public interface PersonaRepository extends GenericRepository<Persona>{
 public Optional<Persona> findBycedula(String cedula);
 public Boolean existsBycedula(String cedula);
}
