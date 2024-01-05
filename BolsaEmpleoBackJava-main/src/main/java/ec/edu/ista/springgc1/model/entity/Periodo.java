package ec.edu.ista.springgc1.model.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;

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
	 private String nombre;
}
