package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import javax.persistence.*;

import org.hibernate.annotations.ColumnTransformer;

import com.fasterxml.jackson.annotation.JsonView;

import ec.edu.ista.springgc1.view.View;

@Data
@Entity
@Table(name = "ciudad")
public class Ciudad {
	@JsonView(View.City.class) 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_ciudad")
	private Long id;
	
	@JsonView(View.City.class)
	@Column(unique = true)
	@ColumnTransformer(write = "UPPER(?)")
	private String nombre;
	@ManyToOne
	@JoinColumn(name = "id_provincia", referencedColumnName = "id_provincia")
	private Provincia provincia;

}
