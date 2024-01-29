package ec.edu.ista.springgc1.model.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;
import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import ec.edu.ista.springgc1.view.View;
import lombok.Data;

@Data
@Entity
@Table(name = "ofertaslaborales")
public class OfertasLaborales {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "oferta_id")
	@JsonView(View.Base.class)
	private Long id;
	
	@JsonView(View.Base.class)
	private double salario;
	
	@JsonView(View.Base.class)
	private LocalDate fecha_cierre;
	
	@Column(name = "fechaPublicacion")
	@JsonView(View.Base.class)
	private LocalDate fechaPublicacion;
	
	@ColumnTransformer(write = "UPPER(?)")
	@JsonView(View.Base.class)
	private String cargo;
	
	@JsonView(View.Base.class)
	@ColumnTransformer(write = "UPPER(?)")
	private String experiencia;
	
	@JsonView(View.Base.class)
	private LocalDate fecha_apertura;
	
	@JsonView(View.Base.class)
	private String area_conocimiento;
	
	@JsonView(View.Base.class)
	private Boolean estado;
	
	@ManyToOne
	@JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
	@JsonView(View.Base.class)
	private Empresa empresa;

	@Nullable
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "ofertas")
	@JsonManagedReference
	private List<Graduado> graduados;
}
