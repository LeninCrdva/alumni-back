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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnTransformer;

import lombok.Data;
@Data
@Entity
@Table(name="referenca_personal")
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
	  @Pattern(regexp = "\\d+", message = "El teléfono debe contener solo dígitos.")
	    @Size(min = 10, max = 10, message = "El teléfono debe tener exactamente 10 dígitos.")
	    @Column(name = "telefono", nullable = false, length = 10)
	 private  String telefono;
	 @Email(message = "Debe ser una dirección de correo electrónico válida.")
	    @Column(name = "email", nullable = false, length = 255)
	 private String email;
	 
}