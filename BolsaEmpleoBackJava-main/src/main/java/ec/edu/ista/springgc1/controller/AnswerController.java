package ec.edu.ista.springgc1.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.ista.springgc1.model.dto.AnswerSearchDTO;
import ec.edu.ista.springgc1.model.dto.QuestionWithAnswersDTO;
import ec.edu.ista.springgc1.model.dto.SurveyQuestionsAnswersDTO;
import ec.edu.ista.springgc1.model.entity.Answer;
import ec.edu.ista.springgc1.model.entity.Question;
import ec.edu.ista.springgc1.model.entity.Survey;
import ec.edu.ista.springgc1.repository.AnswerRepository;
import ec.edu.ista.springgc1.repository.SurveyRepository;
import ec.edu.ista.springgc1.service.impl.AnswerServiceImp;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/answer")
public class AnswerController {
	
	 @Autowired
	    private AnswerServiceImp answerService;
	 @Autowired
	    private AnswerRepository answerRepository;
	  @Autowired
	    private SurveyRepository surveyRepository;
	 // Endpoint para guardar una respuesta
    @PostMapping("/save")
    public ResponseEntity<Answer> saveAnswer(@RequestBody AnswerSearchDTO answerDTO) {
        Answer savedAnswer = answerService.saveAnswer(answerDTO);
        return new ResponseEntity<>(savedAnswer, HttpStatus.CREATED);
    }

    @GetMapping("/survey/{surveyId}/questions-answers")
    public ResponseEntity<Map<String, Object>> getQuestionsWithAnswersBySurveyId(@PathVariable Long surveyId) {
        Optional<Survey> optionalSurvey = surveyRepository.findById(surveyId);
        if (optionalSurvey.isPresent()) {
            Survey survey = optionalSurvey.get();
            List<Question> questions = survey.getQuestions();
            List<Answer> answers = answerRepository.findBySurveyId(surveyId);

            List<Map<String, Object>> questionWithAnswersList = new ArrayList<>();
            for (Question question : questions) {
                Map<String, Object> questionWithAnswers = new HashMap<>();
                questionWithAnswers.put("questionId", question.getId());

                List<String> questionAnswers = new ArrayList<>();
                for (Answer answer : answers) {
                    if (answer.getAnswers().containsKey(question.getId())) {
                        questionAnswers.add(answer.getAnswers().get(question.getId()));
                    }
                }
                questionWithAnswers.put("answers", questionAnswers);

                questionWithAnswersList.add(questionWithAnswers);
            }

            Map<String, Object> response = new LinkedHashMap<>(); 
            response.put("surveyId", surveyId);
            response.put("surveyTitle", survey.getTitle()); 
            response.put("surveyDescription", survey.getDescription());
            response.put("questionsWithAnswers", questionWithAnswersList);

            return ResponseEntity.ok(response);
        } else {
            throw new IllegalArgumentException("No se encontró la encuesta con ID: " + surveyId);
        }
    }


    @GetMapping("/all-surveys-questions-answers")
    public ResponseEntity<List<SurveyQuestionsAnswersDTO>> getAllSurveysWithQuestionsAndAnswers() {
        List<SurveyQuestionsAnswersDTO> surveyQuestionsAnswersList = answerService.loadAllSurveysWithQuestionsAndAnswers();
        return ResponseEntity.ok(surveyQuestionsAnswersList);
    }




  

}
