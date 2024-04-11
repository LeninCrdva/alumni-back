package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonView;

import ec.edu.ista.springgc1.model.entity.Survey;
import ec.edu.ista.springgc1.view.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerSearchDTO implements Serializable {
	 private Long id;
	    private String graduadoEmail;
	    private String carreraNombre;
	    private String surveyTitle; 
	    private Map<Long, String> questionResponses; 
	    private String openAnswer;

}
