package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.ColumnTransformer;

@Data
@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long id;

    @NotEmpty
    @Column(nullable = false, length = 20)
    @ColumnTransformer(write = "UPPER(?)")
    private String nombre;

    @NotEmpty
    @ColumnTransformer(write = "UPPER(?)")
    @Column(length = 255)
    private String descripcion;
}
