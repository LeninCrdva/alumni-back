package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ec.edu.ista.springgc1.model.entity.Ciudad;
import ec.edu.ista.springgc1.model.entity.Empresario;
import ec.edu.ista.springgc1.model.entity.SectorEmpresarial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaDTO implements Serializable {

    private Long id;

    private String empresario;

    private Ciudad ciudad;

    private SectorEmpresarial sectorEmpresarial;

    private String ruc;

    private String nombre;

    private String tipoEmpresa;

    private String razonSocial;

    private String area;

    private String sitioWeb;

    private String ubicacion;

    private boolean estado;
}
