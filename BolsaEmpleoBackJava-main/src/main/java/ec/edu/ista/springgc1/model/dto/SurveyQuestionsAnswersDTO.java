package ec.edu.ista.springgc1.model.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class SurveyQuestionsAnswersDTO implements Serializable{
	 private Long surveyId;
	    private String surveyTitle;
	    private String surveyDescription;
	    private List<QuestionWithAnswersDTO> questionsWithAnswers;
}
