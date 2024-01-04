package ec.edu.ista.springgc1.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class ExperienciaDTO implements Serializable {

    private  Long id;

    @NotEmpty
    private  String cedula;
    @NotEmpty
    private  String area_trabajo;
    @NotEmpty
    private String institucion;
    @NotEmpty
    private  String cargo;
    @NotEmpty
    private  String duracion;
    @NotEmpty
    private  String actividad;





}
