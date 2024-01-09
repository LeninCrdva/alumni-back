package ec.edu.ista.springgc1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import ec.edu.ista.springgc1.model.entity.Administrador;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;

public interface AdministradorRepository extends GenericRepository<Administrador>{
	 @Query(value = "select * from estudiante where usuario_id = :id_usuario", nativeQuery = true)
	    Optional<Administrador> findByUsuario(long id_usuario);
}
