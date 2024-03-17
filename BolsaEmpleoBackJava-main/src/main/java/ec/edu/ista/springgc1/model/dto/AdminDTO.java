package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AdminDTO implements Serializable {
    private Long id;

    @NotEmpty
    private String usuario;

    @NotNull
    private boolean estado;

    @NotEmpty
    private String cargo;

    @NotEmpty
    private String email;
}
