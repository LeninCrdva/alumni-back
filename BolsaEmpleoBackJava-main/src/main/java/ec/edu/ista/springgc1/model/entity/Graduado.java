package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

import java.time.LocalDate;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name="graduado")
public class Graduado {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "graduado_id")
	    private Long id;
	 @OneToOne
	    @JoinColumn(name = "id_persona", referencedColumnName = "cod_perso")
	   private Persona persona;
	 @ManyToOne
	 @JoinColumn(name = "id_ciudad", referencedColumnName = "id_ciudad")
	 private Ciudad ciudad;
	 private LocalDate año_graduacion;
	 private String email_personal;
	 private String ruta_pdf;
	 @Transient
	 private String url_pdf;
	 
	
	 
	    
}
