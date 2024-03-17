package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import ec.edu.ista.springgc1.model.entity.Empresa;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class OfertasLaboralesDTO implements Serializable {

    private Long id;

    private double salario;

    @DateTimeFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDate fechaCierre;

    @DateTimeFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDate fechaPublicacion;

    private String cargo;

    private String experiencia;

    @DateTimeFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDate fechaApertura;

    private String areaConocimiento;

    private Boolean estado;

    private String nombreEmpresa;

    private Set<String> correoGraduado;
}