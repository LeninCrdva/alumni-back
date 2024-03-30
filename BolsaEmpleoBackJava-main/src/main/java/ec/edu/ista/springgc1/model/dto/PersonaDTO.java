package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class PersonaDTO  implements Serializable{

	private Long id;
	
    @NotEmpty
	private String cedula;

    @NotEmpty
	private String primerNombre;

    @NotEmpty
	private String segundoNombre;

	@DateTimeFormat(pattern = "YYYY-MM-dd")
	private LocalDate fechaNacimiento;

    @NotEmpty
	private String telefono;

    @NotEmpty
	private String apellidoPaterno;

    @NotEmpty
	private String apellidoMaterno;
}
