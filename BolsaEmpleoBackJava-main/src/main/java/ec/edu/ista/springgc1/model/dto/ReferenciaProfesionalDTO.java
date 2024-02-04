package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ReferenciaProfesionalDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3533139284189840443L;

	private Long id;
	private String graduado;
	private String nombre;
	// @NotEmpty
	private String institucion;
	private String email;
}
