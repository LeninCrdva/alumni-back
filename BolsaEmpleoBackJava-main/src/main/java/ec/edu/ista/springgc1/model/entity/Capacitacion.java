package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@Table(name = "capacitacion")
public class Capacitacion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_capacitacion")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "graduado_id", referencedColumnName = "graduado_id")
	private Graduado graduado;
	@DateTimeFormat(pattern = "YYYY-MM-dd")
	private LocalDate fecha_inicio;
	@DateTimeFormat(pattern = "YYYY-MM-dd")
	private LocalDate fecha_fin;
	@ColumnTransformer(write = "UPPER(?)")
	private String nombre;
	@ColumnTransformer(write = "UPPER(?)")
	private String institucion;
	private int horas;
	@ColumnTransformer(write = "UPPER(?)")
	private String tipo_certificado;

}
