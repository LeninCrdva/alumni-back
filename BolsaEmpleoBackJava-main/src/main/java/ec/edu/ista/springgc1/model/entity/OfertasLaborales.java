package ec.edu.ista.springgc1.model.entity;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonView;
import ec.edu.ista.springgc1.view.View;
import org.hibernate.annotations.ColumnTransformer;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

@Data
@Entity
@Table(name = "ofertaslaborales")
public class OfertasLaborales {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "oferta_id")
	@JsonView({View.Postulacion.class, View.Public.class})
	private Long id;

	@JsonView({View.Postulacion.class, View.Public.class})
	private double salario;

	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	@JsonView({View.Postulacion.class, View.Public.class})
	private LocalDateTime fechaCierre;

	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	@JsonView({View.Postulacion.class, View.Public.class})
	private LocalDateTime fechaPublicacion;
	
	@ColumnTransformer(write = "UPPER(?)")
	@JsonView({View.Postulacion.class, View.Public.class})
	private String cargo;
	
	@ColumnTransformer(write = "UPPER(?)")
	@JsonView({View.Postulacion.class, View.Public.class})
	private String tiempo;

	@ColumnTransformer(write = "UPPER(?)")
	@JsonView({View.Postulacion.class, View.Public.class})
	private String experiencia;

	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	@JsonView({View.Postulacion.class, View.Public.class})
	private LocalDateTime fechaApertura;

	@JsonView({View.Postulacion.class, View.Public.class})
	private String areaConocimiento;

	@JsonView({View.Postulacion.class, View.Public.class})
	private Boolean estado;
	
	@ManyToOne
	@JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
	@JsonView({View.Postulacion.class, View.Public.class})
	private Empresa empresa;
	
	@Column(name = "tipo")
	@JsonView({View.Postulacion.class, View.Public.class})
	private String tipo;
	
	@Column(name = "foto_portada", columnDefinition = "LONGBLOB")
	@JsonView({View.Postulacion.class, View.Public.class})
	private String fotoPortada;
}
