package ec.edu.ista.springgc1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.model.entity.OfertasLaborales;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;

public interface OfertaslaboralesRepository extends GenericRepository<OfertasLaborales> {
	List<OfertasLaborales> findOfertasByIdIn(List<Long> idOfertas);

	List<OfertasLaborales> findByGraduados_Usuario_NombreUsuario(String nombreUsuario);
	
	@Query("SELECT o FROM OfertasLaborales o JOIN o.empresa e JOIN e.empresario em JOIN em.usuario u WHERE UPPER(u.nombreUsuario) = UPPER(:nombreUsuario)")
	List<OfertasLaborales> buscarOfertasPorNombreUsuario(@Param("nombreUsuario") String nombreUsuario);
	
	 @Query("SELECT o.graduados FROM OfertasLaborales o WHERE o.id = :ofertaId")
	    List<Graduado> findGraduadosByOfertaId(@Param("ofertaId") Long ofertaId);
	 @Query("SELECT o FROM OfertasLaborales o JOIN o.empresa e WHERE UPPER(e.nombre) = UPPER(:nombreEmpresa)")
	    List<OfertasLaborales> findOfertasByNombreEmpresa(@Param("nombreEmpresa") String nombreEmpresa);
}
