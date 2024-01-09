package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;

import ec.edu.ista.springgc1.model.entity.Usuario;
import lombok.Data;
@Data
public class GraduadoDTO implements Serializable{
	  private Long id;
	  @NotEmpty
	  private String usuario;
	  @NotEmpty
		 private String ciudad;
		 private LocalDate año_graduacion;
		 @NotEmpty
		 private String email_personal;
		 @NotEmpty
		 private String estadocivil;
		 private String ruta_pdf;
		 private String url_pdf;
	  
}
