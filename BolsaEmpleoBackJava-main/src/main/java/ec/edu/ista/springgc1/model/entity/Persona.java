package ec.edu.ista.springgc1.model.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import lombok.Data;

@Data
@Entity
@Table(name = "persona")
public class Persona {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "cod_perso")
	    private Long id;
	    @Column(name="cedula",unique = true,nullable = false)
	    private String cedula;
	    private String primer_nombre;
	    private String segundo_nombre;
	    @Column(name = "fecha_nacimiento",nullable = false)
	    private LocalDate fechaNacimiento;
	    private String telefono;
	    private String apellido_paterno;
	    private String apellido_materno;
		@ManyToOne
		@JoinColumn(referencedColumnName = "id_usuario")
		Usuario usuario;
}
