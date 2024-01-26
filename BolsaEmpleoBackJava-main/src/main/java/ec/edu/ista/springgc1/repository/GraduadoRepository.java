package ec.edu.ista.springgc1.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.model.entity.OfertasLaborales;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;

import java.time.LocalDate;


@Repository
public interface GraduadoRepository extends GenericRepository<Graduado> {

	@Query(value = "select * from estudiante where usuario_id = :id_usuario", nativeQuery = true)
	Optional<Graduado> findByUsuario(long id_usuario);

	Optional<Graduado> findByUsuarioPersonaCedulaContaining(String cedula);
	
	List<Graduado> findByEmailPersonalIn(Set<String> email_personal);
}
