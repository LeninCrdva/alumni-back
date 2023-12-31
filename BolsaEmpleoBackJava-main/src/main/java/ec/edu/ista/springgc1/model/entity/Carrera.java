package ec.edu.ista.springgc1.model.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;

import lombok.Data;
@Data
@Entity
@Table(name="carrera")
public class Carrera {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_carrera")
	    private Long id;
	  @ColumnTransformer(write = "UPPER(?)")
	    private String nombre;
	  @ColumnTransformer(write = "UPPER(?)")
	    private String descripcion;
}

