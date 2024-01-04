package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
@Table(name="postulado")
public class Postulado {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "postulacion_id")
	    private Long id;
	 @ManyToOne
	 @JoinColumn(name = "graduado_id", referencedColumnName = "graduado_id")
	 private Graduado graduado;
	 @ManyToOne
	 @JoinColumn(name = "oferta_id", referencedColumnName = "oferta_id")
	 private OfertasLaborales ofertaslaborales;
	 

}

