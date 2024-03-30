package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import ec.edu.ista.springgc1.model.entity.Usuario;
import lombok.Data;

@Data
public class GraduadoDTO implements Serializable {
	private Long id;

	@NotEmpty
	private String usuario;

	@NotEmpty
	private String ciudad;

	@DateTimeFormat(pattern = "YYYY-MM-dd")
	private LocalDate anioGraduacion;

	@NotEmpty
	private String emailPersonal;

	@NotEmpty
	private String estadoCivil;

	private String rutaPdf;

	private String urlPdf;
}
