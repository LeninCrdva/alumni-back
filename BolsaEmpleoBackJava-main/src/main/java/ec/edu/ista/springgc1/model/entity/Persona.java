package ec.edu.ista.springgc1.model.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "persona")
public class Persona {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cod_perso")
	private Long id;
	@Column(name = "cedula", unique = true, nullable = false)
	@Size(min = 10, max = 10, message = "La cédula debe tener exactamente 10 dígitos")
	@Pattern(regexp = "\\d+", message = "La cédula debe contener solo dígitos")
	private String cedula;
	private String primer_nombre;
	private String segundo_nombre;
	@Column(name = "fechaNacimiento", nullable = false)
	private LocalDate fechaNacimiento;
	@Size(min = 10, max = 10, message = "El número de celular debe tener exactamente 10 dígitos")
	@Pattern(regexp = "\\d+", message = "El número de celular debe contener solo dígitos")
	private String telefono;
	private String apellido_paterno;
	private String apellido_materno;
}
