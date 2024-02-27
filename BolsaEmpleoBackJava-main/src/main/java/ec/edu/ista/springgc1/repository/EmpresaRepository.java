package ec.edu.ista.springgc1.repository;


import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ec.edu.ista.springgc1.model.entity.Empresa;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;

public interface EmpresaRepository extends GenericRepository<Empresa>{
	Optional<Empresa> findByNombre(String nombre);
	 public Boolean existsBynombre(String name);
	  @Query("SELECT e FROM Empresa e JOIN e.empresario u WHERE UPPER(u.usuario.nombreUsuario) = UPPER(:nombreUsuario)")
	    Set<Empresa> findByNombreUsuario(@Param("nombreUsuario") String nombreUsuario);
}
