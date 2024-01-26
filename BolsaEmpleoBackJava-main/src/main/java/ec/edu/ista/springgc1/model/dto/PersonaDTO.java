package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
@Data
public class PersonaDTO  implements Serializable{
	

	private Long id;
	
    @NotEmpty
	private String cedula;
    @NotEmpty
	private String primer_nombre;
    @NotEmpty
	private String segundo_nombre;
    
	private LocalDate fechaNacimiento;
    @NotEmpty
	private String telefono;
    @NotEmpty
	private String apellido_paterno;
    @NotEmpty
	private String apellido_materno;
}
