package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ec.edu.ista.springgc1.model.entity.Graduado;
import lombok.Data;
@Data
public class TituloDTO implements Serializable{
	  private Long id;
	    @NotNull
	    private Long idgraduado;
	    @NotNull
	    private String tipo;
	    @NotNull
	    private String nivel;
	    @NotNull
	    private String institucion;
	    @NotNull
	    private String nombre_titulo;
	    @NotNull
	    private LocalDate fecha_emision;
	    @NotNull
		 private LocalDate fecha_registro;
	    @NotNull
		 private String num_registro;
	    @NotNull
		 private String nombrecarrera;
	   
}
