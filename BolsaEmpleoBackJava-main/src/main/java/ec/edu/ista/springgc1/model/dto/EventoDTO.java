package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventoDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 2477337056172110398L;

	private Long id;
	@NotEmpty
	private String nombreEvento;
	//@DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss")
	private String horaInicio;
	//@DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss")
	private String horaFin;
	//@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fecha;
	@NotEmpty
	private String lugar;
	
	@NotEmpty
	private String cedulaAdmin;
	@NotEmpty
	private String descripcion;
}
