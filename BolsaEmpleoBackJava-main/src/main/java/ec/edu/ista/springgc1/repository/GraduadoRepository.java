package ec.edu.ista.springgc1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;

@Repository
public interface GraduadoRepository  extends GenericRepository<Graduado>{

	 @Query(value = "select * from estudiante where usuario_id = :id_usuario", nativeQuery = true)
	    Optional<Graduado> findByUsuario(long id_usuario);
}
