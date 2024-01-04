package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.Date;

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
@Table(name="capacitacion")
public class Capacitacion {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_capacitacion")
	    private Long id;
	 @ManyToOne
	 @JoinColumn(name = "graduado_id", referencedColumnName = "graduado_id")
	 private Graduado graduado;
	 private LocalDate fecha_inicio;
	 private LocalDate fecha_fin;
	 private String nombre;
	 private String institucion;
	 private int horas;
	 private String tipo_certificado;
	 
}


