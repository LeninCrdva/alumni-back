package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.ColumnTransformer;

@Data
@Entity
@Table(name = "areaTrabajo")
public class AreaTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "areaTrabajo_id", nullable = false)
    private Long id;

    @NotEmpty
    @Column(nullable = false, length = 30)
    @ColumnTransformer(write = "UPPER(?)")
    private String nombre;



}
