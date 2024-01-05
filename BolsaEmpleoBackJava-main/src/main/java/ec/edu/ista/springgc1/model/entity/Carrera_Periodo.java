package ec.edu.ista.springgc1.model.entity;



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
@Table(name="carrera_periodo")
public class Carrera_Periodo {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_carre_peri")
	    private Long id;
	 @ManyToOne
	 @JoinColumn(name = "id_carrera", referencedColumnName = "id_carrera")
	 private Carrera carrera;
	 @ManyToOne
	 @JoinColumn(name = "id_periodo", referencedColumnName = "id_periodo")
	 private Periodo periodo;
}