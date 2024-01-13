package ec.edu.ista.springgc1.model.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;

import lombok.Data;
@Data
@Entity
@Table(name="periodo")
public class Periodo {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_periodo")
	    private Long id;
	 private LocalDate fecha_inicio;
	 private LocalDate fecha_fin;
	 private Boolean estado=true;
	  @ColumnTransformer(write = "UPPER(?)")
	 private String nombre;
	  @ManyToMany
	    @JoinTable(
	            name = "periodo_carrera",
	            joinColumns = @JoinColumn(name = "id_periodo"),
	            inverseJoinColumns = @JoinColumn(name = "id_carrera"))
	    private Set<Carrera> carreras = new HashSet<>();
}
