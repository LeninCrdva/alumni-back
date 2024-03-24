package ec.edu.ista.springgc1.model.entity;

import ec.edu.ista.springgc1.model.enums.QuestionType;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @ElementCollection
    private List<String> options;
}
