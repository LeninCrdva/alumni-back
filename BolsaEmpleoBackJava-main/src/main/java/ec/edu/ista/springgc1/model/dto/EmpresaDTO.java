package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ec.edu.ista.springgc1.model.entity.Ciudad;
import ec.edu.ista.springgc1.model.entity.Empresario;
import ec.edu.ista.springgc1.model.entity.SectorEmpresarial;
import lombok.Data;
@Data
public class EmpresaDTO implements Serializable{
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
	    // Atributos para save and update
	   // private Long ciudadId;
	    //private Long sectorEmpresarialId;
	    //private Long empresarioId;
	    
	    // Constructor para guardar/actualizar
	    /*public EmpresaDTO(Long id, String ruc, String nombre, String tipoEmpresa, String razonSocial, String area, String sitioWeb, Long ciudadId, Long sectorEmpresarialId, Long empresarioId,String ubicacion) {
	        this.id = id;
	        this.ruc = ruc;
	        this.nombre = nombre;
	        this.tipoEmpresa = tipoEmpresa;
	        this.razonSocial = razonSocial;
	        this.area = area;
	        this.sitioWeb = sitioWeb;
	        this.ciudadId = ciudadId;
	        this.sectorEmpresarialId = sectorEmpresarialId;
	        this.empresarioId = empresarioId;
	        this.ubicacion= ubicacion;
	    }*/
	    //listar y buscar
	    public EmpresaDTO(Long id, String ruc, String nombre, String tipoEmpresa, String razonSocial, String area, String sitioWeb, Ciudad ciudad, SectorEmpresarial sectorEmpresarial, String empresario,String ubicacion) {
	        this.id = id;
	        this.ruc = ruc;
	        this.nombre = nombre;
	        this.tipoEmpresa = tipoEmpresa;
	        this.razonSocial = razonSocial;
	        this.area = area;
	        this.sitioWeb = sitioWeb;
	        this.ciudad = ciudad;
	        this.sectorEmpresarial = sectorEmpresarial;
	        this.empresario = empresario;
	        this.ubicacion= ubicacion;
	    }
	    public EmpresaDTO(){
	    	
	    }
}
