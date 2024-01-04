package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name="experiencia")
public class Experiencia {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_experiencia")
	    private Long id;
	 @ManyToOne
	 @JoinColumn(name = "graduado_id", referencedColumnName = "graduado_id")
	 private Graduado graduado;
	 private String cargo;
	 private String duracion;
	 private String institucion;
	 private String actividad;
	 
	 
}
