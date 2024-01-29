package ec.edu.ista.springgc1.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import org.hibernate.annotations.ColumnTransformer;

import lombok.Data;

@Data
@Entity
@Table(name="empresario")
public class Empresario {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_empre")
	    private Long id;
	 @OneToOne
	    @JoinColumn(referencedColumnName = "id_usuario")
	   private Usuario usuario;
	 private boolean estado;
	  @ColumnTransformer(write = "UPPER(?)")
	 private String puesto;
	 private int anios;
	   @Email(message = "Debe ser una dirección de correo electrónico válida.")
	    @Column(name = "email", nullable = false, length = 255)
	    private String email;

}
