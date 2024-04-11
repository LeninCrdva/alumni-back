package ec.edu.ista.springgc1.repository;

import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.model.entity.Titulo;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TituloRepository extends GenericRepository<Titulo>{
    List<Titulo> findAllByGraduadoId(Long id);
    
    @Query("SELECT t.graduado FROM Titulo t WHERE UPPER(t.carrera.nombre) = UPPER(:nombreCarrera)")
    List<Graduado> findAllGraduadosByNombreCarrera(@Param("nombreCarrera") String nombreCarrera);
    
    @Query("SELECT t.carrera.nombre, g.usuario.persona.sexo, COUNT(g) " +
            "FROM Titulo t " +
            "JOIN t.graduado g " +
            "GROUP BY t.carrera.nombre, g.usuario.persona.sexo")
     List<Object[]> contarGraduadosPorSexoPorCarrera();

}
