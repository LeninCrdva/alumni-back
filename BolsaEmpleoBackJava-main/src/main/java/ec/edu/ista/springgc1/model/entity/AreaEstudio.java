package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.ColumnTransformer;

@Data
@Entity
@Table(name = "areaEstudio")
public class AreaEstudio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "areaEstudio_id", nullable = false)
    private Long id;

    @NotEmpty
    @Column(nullable = false, length = 100)
    @ColumnTransformer(write = "UPPER(?)")
    private String nombre;
}
