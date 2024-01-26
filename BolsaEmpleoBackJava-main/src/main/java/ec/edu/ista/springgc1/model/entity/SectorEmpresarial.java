package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.ColumnTransformer;

@Data
@Entity
@Table(name = "sectorEmpresarial")
public class SectorEmpresarial {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sec_emp_id", nullable = false)
    private Long id;

    
    @Column(length = 255)
    @ColumnTransformer(write = "UPPER(?)")
    private String nombre;

    
    @Column(length = 255)
    @ColumnTransformer(write = "UPPER(?)")
    private String descripcion;


}
