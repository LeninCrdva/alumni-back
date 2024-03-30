package ec.edu.ista.springgc1.model.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.ColumnTransformer;
import org.springframework.format.annotation.DateTimeFormat;
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
	@DateTimeFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private LocalDateTime fechaCierre;

	@JsonView(View.Base.class)
	@DateTimeFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private LocalDateTime fechaPublicacion;
	
	@ColumnTransformer(write = "UPPER(?)")
	@JsonView(View.Base.class)
	private String cargo;
	
	@ColumnTransformer(write = "UPPER(?)")
	@JsonView(View.Base.class)
	private String tiempo;
	
	@JsonView(View.Base.class)
	@ColumnTransformer(write = "UPPER(?)")
	private String experiencia;
	
	@JsonView(View.Base.class)
	@DateTimeFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private LocalDateTime fechaApertura;
	
	@JsonView(View.Base.class)
	private String areaConocimiento;
	
	@JsonView(View.Base.class)
	private Boolean estado;
	
	@ManyToOne
	@JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
	@JsonView(View.Base.class)
	private Empresa empresa;
	
	@Column(name = "tipo")
	@JsonView(View.Base.class)
	private String tipo;
	
	@Column(name = "foto_portada", columnDefinition = "TEXT")
	@JsonView(View.Base.class)
	private String fotoPortada;
}
