package ec.edu.ista.springgc1.model.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name = "ofertaslaborales")
@JsonIgnoreProperties({"graduados"})
public class OfertasLaborales {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "oferta_id")
	private Long id;
	private double salario;
	private LocalDate fecha_cierre;
	@Column(name = "fechaPublicacion")
	private LocalDate fechaPublicacion;
	@ColumnTransformer(write = "UPPER(?)")
	private String cargo;
	@ColumnTransformer(write = "UPPER(?)")
	private String experiencia;
	private LocalDate fecha_apertura;
	private String area_conocimiento;
	private Boolean estado;
	@ManyToOne
	@JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
	private Empresa empresa;
	@Nullable
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "ofertas")
	private List<Graduado> graduados;
}
