package ec.edu.ista.springgc1.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class ExperienciaDTO implements Serializable {

	private Long id;

	private String cedulaGraduado;

	@NotEmpty
	private String area_trabajo;

	@NotEmpty
	private String institucionNombre;

	@NotEmpty
	private String cargo;

	@NotEmpty
	private String duracion;

	@NotEmpty
	private String actividad;
}
