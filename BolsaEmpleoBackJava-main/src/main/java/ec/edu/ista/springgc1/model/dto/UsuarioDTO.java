package ec.edu.ista.springgc1.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

@Data
public class UsuarioDTO implements Serializable {

    private Long id;

    @NotEmpty
    private String nombreUsuario;

    @NotNull
    private String clave;

    @NotNull
    private String cedula;
	
    @NotEmpty
    private String rol;

    @NotNull
    private boolean estado;

    private String rutaImagen;

    private String urlImagen;
}
