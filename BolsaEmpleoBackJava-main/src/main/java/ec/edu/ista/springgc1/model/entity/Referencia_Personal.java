package ec.edu.ista.springgc1.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.ColumnTransformer;
import org.springframework.lang.Nullable;

import lombok.Data;

@Data
@Entity
@Table(name = "referencia_personal")
public class Referencia_Personal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_refe_personal")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "graduado_id", referencedColumnName = "graduado_id")
	private Graduado graduado;
	@ColumnTransformer(write = "UPPER(?)")
	private String nombre;
	@Nullable
	//@Pattern(regexp = "\\d+", message = "El teléfono debe contener solo dígitos.")
	//@Size(min = 10, max = 10, message = "El teléfono debe tener exactamente 10 dígitos.")
	@Column(name = "telefono")
	private String telefono;
	@Nullable
	//@Email(message = "Debe ser una dirección de correo electrónico válida.")
	//@Column(name = "email", nullable = false, length = 255)
	@Column(name = "email")
	private String email;

}