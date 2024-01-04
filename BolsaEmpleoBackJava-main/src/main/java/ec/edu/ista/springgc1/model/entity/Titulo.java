package ec.edu.ista.springgc1.model.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="titulo")
public class Titulo {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_titulo")
	    private Long id;
	 @ManyToOne
	 @JoinColumn(name = "graduado_id", referencedColumnName = "graduado_id")
	 private Graduado graduado;
	 private String tipo;
	 private String nivel;
	 private String institucion;
	 private String nombre_titulo;
	 private LocalDate fecha_emision;
	 private LocalDate fecha_registro;
	 private String num_registro;
	 @ManyToOne
	 @JoinColumn(name = "id_carrera", referencedColumnName = "id_carrera")
	 private Carrera carrera;
	 
	 
}