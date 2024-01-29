package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import javax.persistence.*;

import org.hibernate.annotations.ColumnTransformer;

import com.fasterxml.jackson.annotation.JsonView;

import ec.edu.ista.springgc1.view.View;

@Data
@Entity
@Table(name = "empresa")
public class Empresa {
	@JsonView(View.Public.class) 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_empresa")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_empre", referencedColumnName = "id_empre")
	private Empresario empresario;
	
	@JsonView(View.Public.class)
	@ManyToOne
	@JoinColumn(name = "id_ciudad", referencedColumnName = "id_ciudad")
	private Ciudad ciudad;
	
	@ManyToOne
	@JoinColumn(name = "sec_emp_id", referencedColumnName = "sec_emp_id")
	private SectorEmpresarial sectorEmpresarial;

	@Column(name = "RUC")
	private String ruc;
	
	@JsonView(View.Public.class)
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

	@Column(name = "sitio_web")
	@ColumnTransformer(write = "UPPER(?)")
	private String sitioWeb;
}
