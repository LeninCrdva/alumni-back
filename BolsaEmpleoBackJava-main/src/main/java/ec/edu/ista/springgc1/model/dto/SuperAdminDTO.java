package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ec.edu.ista.springgc1.model.entity.Usuario;
import lombok.Data;
@Data
public class SuperAdminDTO implements Serializable{
	  private Long id;
	  @NotEmpty
	  private String usuario;
	   @NotNull
	  private boolean estado;
}
