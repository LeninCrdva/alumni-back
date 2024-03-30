package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnTransformer;
import org.springframework.lang.Nullable;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long id;

	private String clave;

	@Column(name = "nombre_usuario", unique = true)
	 @ColumnTransformer(write = "UPPER(?)")
	private String nombreUsuario;

	@NotNull
	private Boolean estado;

	@OneToOne
	@NotNull
	@JoinColumn(name = "id_rol", referencedColumnName = "id_rol", nullable = false)
	private Rol rol;

	private String rutaImagen;

	@Transient
	private String urlImagen;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "cod_perso")
	Persona persona;
}
