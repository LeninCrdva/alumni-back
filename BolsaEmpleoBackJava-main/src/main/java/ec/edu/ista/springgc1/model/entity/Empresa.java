package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Long id;
    @ManyToOne
	 @JoinColumn(name = "id_empre", referencedColumnName = "id_empre")
	 private Empresario empresario;
  

    @ManyToOne
	 @JoinColumn(name = "id_ciudad", referencedColumnName = "id_ciudad")
	 private Ciudad ciudad;
	 @ManyToOne
	    @JoinColumn(name = "sec_emp_id", referencedColumnName = "sec_emp_id")
	    private SectorEmpresarial sectorEmpresarial;

    @Column(name = "RUC")
    private String ruc;

    private String nombre;

    @Column(name = "tipo_empresa")
    private String tipoEmpresa;

    @Column(name = "razon_social")
    private String razonSocial;

    private String area;

   

    private String ubicacion;
    private String tipo;

    @Column(name = "sitio_web")
    private String sitioWeb;
}
