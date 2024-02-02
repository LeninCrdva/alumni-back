package ec.edu.ista.springgc1.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;


@Repository
public interface GraduadoRepository extends GenericRepository<Graduado> {

	Optional<Graduado> findByUsuarioId(long id_usuario);

	Optional<Graduado> findByUsuarioPersonaCedulaContaining(String cedula);
	
	List<Graduado> findByEmailPersonalIn(Set<String> email_personal);

	Optional<Graduado> findByUsuarioNombreUsuario(String name);

	@Query("SELECT g FROM Graduado g LEFT JOIN g.ofertas o WHERE o IS NULL")
	List<Graduado> findAllGraduadosWithoutOfertas();
}
