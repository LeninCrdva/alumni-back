package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import javax.persistence.*;

import org.hibernate.annotations.ColumnTransformer;

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
    @ColumnTransformer(write = "UPPER(?)")
    private String nombre;
    @ColumnTransformer(write = "UPPER(?)")
    @Column(name = "tipo_empresa")
    private String tipoEmpresa;
    @ColumnTransformer(write = "UPPER(?)")
    @Column(name = "razon_social")
    private String razonSocial;
    @ColumnTransformer(write = "UPPER(?)")
    private String area;

   
    @ColumnTransformer(write = "UPPER(?)")
    private String ubicacion;
    @ColumnTransformer(write = "UPPER(?)")
    private String tipo;

    @Column(name = "sitio_web")
    @ColumnTransformer(write = "UPPER(?)")
    private String sitioWeb;
}
