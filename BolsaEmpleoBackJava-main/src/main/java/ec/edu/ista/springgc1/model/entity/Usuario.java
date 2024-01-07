package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

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
	private String nombreUsuario;
	private Boolean estado;
	@OneToOne
	@JoinColumn(name = "id_rol", referencedColumnName = "id_rol", nullable = false)
	private Rol rol;
	private String ruta_imagen;
	@Transient
	private String url_imagen;
	@Enumerated(EnumType.STRING)
	private UsuarioTipo usuarioTipo;
	@ManyToOne
	@JoinColumn(referencedColumnName = "cod_perso")
	Persona persona;
}
