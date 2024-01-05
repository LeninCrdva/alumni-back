package ec.edu.ista.springgc1.model.entity;
import java.time.LocalDate;
import java.time.LocalTime;

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
@Table(name="evento")
public class Evento {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_evento")
	    private Long id;
	  private Boolean estado=true;
	  private LocalTime hora_inicio;
	  private LocalTime hora_fin;
	  private LocalDate fecha;
	  private String lugar;
	  private String descripcion;
	  @ManyToOne
		 @JoinColumn(name = "id_admi", referencedColumnName = "id_admi", nullable = true)
		 private Administrador admin;
}



