package ec.edu.ista.springgc1.model.entity;

import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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

	private String primerNombre;

	private String segundoNombre;

	@Column(name = "fecha_nacimiento", nullable = false)
	@DateTimeFormat(pattern = "YYYY-MM-dd")
	private LocalDate fechaNacimiento;

	@Size(min = 10, max = 10, message = "El número de celular debe tener exactamente 10 dígitos")
	@Pattern(regexp = "\\d+", message = "El número de celular debe contener solo dígitos")
	private String telefono;

	private String apellidoPaterno;

	private String apellidoMaterno;

	@Enumerated(EnumType.STRING)
	private Sex sexo;

	public static enum Sex {
		MASCULINO, FEMENINO, OTRO;
	}
}
