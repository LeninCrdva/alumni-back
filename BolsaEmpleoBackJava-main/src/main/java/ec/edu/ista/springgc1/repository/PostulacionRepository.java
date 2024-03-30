package ec.edu.ista.springgc1.repository;

import ec.edu.ista.springgc1.model.entity.Postulacion;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PostulacionRepository extends GenericRepository<Postulacion> {

    List<Postulacion> findAllByOfertaLaboralId(Long ofertaLaboralId);

    List<Postulacion> findAllByGraduadoUsuarioId(Long id);

    List<Postulacion> findAllByGraduadoUsuarioNombreUsuario(String nombreUsuario);

    Integer countPostulacionByFechaPostulacionIsStartingWith(LocalDateTime fechaPostulacion);

    Integer countByGraduadoIdAndOfertaLaboralId(Long idGraduado, Long idOfertaLaboral);

    @Query("SELECT p FROM Graduado G LEFT OUTER JOIN Postulacion p ON g = p.graduado WHERE p.graduado IS NULL")
    List<Postulacion> findGraduadosSinPostulacion();
}
