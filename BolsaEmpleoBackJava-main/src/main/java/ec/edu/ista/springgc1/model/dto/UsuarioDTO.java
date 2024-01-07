package ec.edu.ista.springgc1.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ec.edu.ista.springgc1.model.entity.Persona;
import ec.edu.ista.springgc1.model.entity.UsuarioTipo;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UsuarioDTO implements Serializable {

    private Long id;
    @NotEmpty
    private String nombreUsuario;
    @NotNull
    private String clave;
    /*private Graduado graduado;
    private SuperAdmin superadmin;
    private Administrador admin;
	 private Empresario empresario;*/
    @NotNull
    private String cedula;
	 private UsuarioTipo usuarioTipo;
    @NotEmpty
    private String rol;
    private boolean estado;
    private String ruta_imagen;
    private String url_imagen;
}
