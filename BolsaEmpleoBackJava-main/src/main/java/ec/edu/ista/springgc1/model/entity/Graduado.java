package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;

import org.hibernate.annotations.ColumnTransformer;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Entity
@Table(name = "graduado")
@JsonIgnoreProperties({ "ofertas" })
public class Graduado {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "graduado_id")
	private Long id;
	@OneToOne
	@JoinColumn(referencedColumnName = "id_usuario")
	private Usuario usuario;
	@ManyToOne
	@JoinColumn(name = "id_ciudad", referencedColumnName = "id_ciudad")
	private Ciudad ciudad;
	private LocalDate año_graduacion;

	@Email(message = "Debe ser una dirección de correo electrónico válida.")
	@Column(name = "email_personal", nullable = false, length = 255, unique = true)
	private String emailPersonal;
	@ColumnTransformer(write = "UPPER(?)")
	private String estadocivil;
	private String ruta_pdf;
	@Transient
	private String url_pdf;
	@Nullable
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "postulados", 
			joinColumns = @JoinColumn(name = "graduado_id"), 
			inverseJoinColumns = @JoinColumn(name = "oferta_id"))
	private List<OfertasLaborales> ofertas;
}
