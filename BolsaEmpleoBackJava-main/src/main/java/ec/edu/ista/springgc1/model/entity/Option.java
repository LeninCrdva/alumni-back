package ec.edu.ista.springgc1.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
