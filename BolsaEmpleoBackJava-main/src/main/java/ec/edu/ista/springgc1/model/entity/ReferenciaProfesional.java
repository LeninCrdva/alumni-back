package ec.edu.ista.springgc1.model.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import org.hibernate.annotations.ColumnTransformer;

import lombok.Data;
@Data
@Entity
@Table(name="referencia_profesional")
public class ReferenciaProfesional {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    //@Column(name = "id_ref_profesional")
	    private Long id;
	 @ManyToOne
	 @JoinColumn(name = "graduadoId", referencedColumnName = "graduado_id")
	 private Graduado graduado;
	 @ColumnTransformer(write = "UPPER(?)")
	 private String nombre;
	 @ColumnTransformer(write = "UPPER(?)")
	 private  String institucion;
	 @Email(message = "Debe ser una dirección de correo electrónico válida.")
	    @Column(name = "email", nullable = false, length = 255)
	 private String email;
	 
}
