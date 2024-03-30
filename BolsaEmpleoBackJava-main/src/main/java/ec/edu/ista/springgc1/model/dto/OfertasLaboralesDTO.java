package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class OfertasLaboralesDTO implements Serializable {

    private Long id;

    private double salario;

    @DateTimeFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime fechaCierre;

    @DateTimeFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime fechaPublicacion;

    private String cargo;

    private String tiempo; 

    private String experiencia;

    @DateTimeFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime fechaApertura;

    private String areaConocimiento;

    private Boolean estado;

    private String nombreEmpresa;
    
    private String fotoPortada;

    private String tipo;
}