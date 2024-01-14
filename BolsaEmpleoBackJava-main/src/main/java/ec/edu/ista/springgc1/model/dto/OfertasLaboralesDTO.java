package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import ec.edu.ista.springgc1.model.entity.Empresa;
import lombok.Data;

@Data
public class OfertasLaboralesDTO implements Serializable {
    private Long id;
    private double salario;
    private LocalDate fechaCierre;
    private LocalDate fechaPublicacion;
    private String cargo;
    private String experiencia;
    private LocalDate fechaApertura;
    private String areaConocimiento;
    private Boolean estado;
    private String nombreEmpresa;
    private Set<String> correoGraduado;
    
}