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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnTransformer;

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
	 @ColumnTransformer(write = "UPPER(?)")
	 private String tipo;
	 @ColumnTransformer(write = "UPPER(?)")
	 private String nivel;
	 @ColumnTransformer(write = "UPPER(?)")
	 private String institucion;
	 @ColumnTransformer(write = "UPPER(?)")
	 private String nombre_titulo;
	 private LocalDate fecha_emision;
	 private LocalDate fecha_registro;
	    @Column(name = "num_registro", nullable = false, length = 20)
	    @Size(min = 5, max = 20, message = "El número de registro debe tener exactamente min 5 dígitos maximo 20")
	    @Pattern(regexp = "\\d+", message = "El número de registro debe contener solo dígitos.")
	    private String num_registro;
	 @ManyToOne
	 @JoinColumn(name = "id_carrera", referencedColumnName = "id_carrera")
	 private Carrera carrera;
	 
	 
}