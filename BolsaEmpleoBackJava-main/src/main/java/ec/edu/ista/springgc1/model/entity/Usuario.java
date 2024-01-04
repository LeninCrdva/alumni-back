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
	    @Column(name="nombre_usuario",unique = true)
	    private String nombreUsuario;
	    private Boolean estado;
	    @OneToOne
	    @JoinColumn(name = "id_rol", referencedColumnName = "id_rol",nullable = false)
	    private Rol rol;
	    private String ruta_imagen;
		 @Transient
		 private String url_imagen;
		 @ManyToOne
		 @JoinColumn(name = "graduado_id", referencedColumnName = "graduado_id", nullable = true)
		 private Graduado graduado;
		 @ManyToOne
		 @JoinColumn(name = "id_super_admi", referencedColumnName = "id_super_admi", nullable = true)
		 private SuperAdmin superadmin;
		 @ManyToOne
		 @JoinColumn(name = "id_admi", referencedColumnName = "id_admi", nullable = true)
		 private Administrador admin;
		 @ManyToOne
		 @JoinColumn(name = "id_empre", referencedColumnName = "id_empre", nullable = true)
		 private Empresario empresario;
		 
		    @Enumerated(EnumType.STRING)
		    private UsuarioTipo usuarioTipo;

}
