package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;
}
