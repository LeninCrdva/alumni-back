package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;

import lombok.Data;
@Data
public class Evento_MDTO implements Serializable{
	  private Long id_prom;

	    private String titulo;

	    private String subTitulo;

	    private String resumen;

	    private String colorFondo;

	    private byte[] foto_portada;

	    private String tipoxml;
}
