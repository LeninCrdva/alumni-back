package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class QuestionWithAnswersDTO implements Serializable {
    private Long questionId;
    private String surveytitle;
    private String suverydescricpion;
    private String questionText;
    private List<String> answers;
}