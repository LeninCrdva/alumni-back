package ec.edu.ista.springgc1.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ec.edu.ista.springgc1.model.dto.AnswerSearchDTO;
import ec.edu.ista.springgc1.model.dto.GraduadoWithUnansweredSurveysDTO;
import ec.edu.ista.springgc1.model.dto.QuestionWithAnswersDTO;
import ec.edu.ista.springgc1.model.dto.SurveyQuestionsAnswersDTO;
import ec.edu.ista.springgc1.model.entity.Answer;
import ec.edu.ista.springgc1.model.entity.Question;
import ec.edu.ista.springgc1.model.entity.Survey;
import ec.edu.ista.springgc1.repository.AnswerRepository;
import ec.edu.ista.springgc1.repository.SurveyRepository;
import ec.edu.ista.springgc1.service.impl.AnswerServiceImp;

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
	  @PreAuthorize("hasRole('GRADUADO')")
    @PostMapping("/save")
    public ResponseEntity<Answer> saveAnswer(@RequestBody AnswerSearchDTO answerDTO) {
        Answer savedAnswer = answerService.saveAnswer(answerDTO);
        return new ResponseEntity<>(savedAnswer, HttpStatus.CREATED);
    }
	  
	  @PreAuthorize("hasRole('ADMINISTRADOR')")
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

	    @PreAuthorize("hasAnyRole('GRADUADO', 'ADMINISTRADOR')")
    @GetMapping("/all-surveys-questions-answers")
    public ResponseEntity<List<SurveyQuestionsAnswersDTO>> getAllSurveysWithQuestionsAndAnswers() {
        List<SurveyQuestionsAnswersDTO> surveyQuestionsAnswersList = answerService.loadAllSurveysWithQuestionsAndAnswers();
        return ResponseEntity.ok(surveyQuestionsAnswersList);
    }
    
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/survey-questions-answers-by-career-coments")
    public ResponseEntity<Map<String, Map<String, List<String>>>> getSurveyQuestionsAnswersByCareercoment() {
        List<Survey> allSurveys = surveyRepository.findAll();
        Map<String, Map<String, List<String>>> surveyByCareerMap = new HashMap<>();

        for (Survey survey : allSurveys) {
            List<Answer> answers = answerRepository.findBySurveyId(survey.getId());
            Map<String, List<String>> surveyCommentsMap = new HashMap<>();

            for (Answer answer : answers) {
                String career = answer.getCarrera().getNombre(); // Carrera asociada a la respuesta
                List<String> careerComments = surveyCommentsMap.computeIfAbsent(career, k -> new ArrayList<>());

                // Obtener el comentario abierto de la respuesta y agregarlo a la lista de comentarios de la carrera
                String openAnswer = answer.getOpenAnswer();
                if (openAnswer != null && !openAnswer.isEmpty()) {
                    careerComments.add(openAnswer);
                }
            }

            // Agregar el mapa de comentarios de la encuesta al mapa de carreras
            for (Map.Entry<String, List<String>> entry : surveyCommentsMap.entrySet()) {
                String career = entry.getKey();
                if (!surveyByCareerMap.containsKey(career)) {
                    surveyByCareerMap.put(career, new HashMap<>());
                }
                surveyByCareerMap.get(career).put(survey.getTitle(), entry.getValue());
            }
        }

        return ResponseEntity.ok(surveyByCareerMap);
    }
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/survey-questions-answers-by-career")
    public ResponseEntity<Map<String, Map<String, List<QuestionWithAnswersDTO>>>> getSurveyQuestionsAnswersByCareer() {
        List<Survey> allSurveys = surveyRepository.findAll();
        Map<String, Map<String, List<QuestionWithAnswersDTO>>> surveyByCareerMap = new HashMap<>();

        for (Survey survey : allSurveys) {
            List<QuestionWithAnswersDTO> questionsWithAnswers = new ArrayList<>();
            List<Question> questions = survey.getQuestions();
            List<Answer> answers = answerRepository.findBySurveyId(survey.getId());

            for (Question question : questions) {
                List<String> questionAnswers = answers.stream()
                    .filter(answer -> answer.getSurvey().getId().equals(survey.getId()) && answer.getAnswers().containsKey(question.getId()))
                    .map(answer -> answer.getAnswers().get(question.getId()))
                    .collect(Collectors.toList());

                QuestionWithAnswersDTO questionDTO = new QuestionWithAnswersDTO();
                questionDTO.setQuestionId(question.getId());
                questionDTO.setQuestionText(question.getText());
                questionDTO.setAnswers(questionAnswers);

                questionsWithAnswers.add(questionDTO);
            }

            for (Answer answer : answers) {
                String career = answer.getCarrera().getNombre(); // Suponiendo que cada respuesta tiene una carrera asociada
                if (!surveyByCareerMap.containsKey(career)) {
                    surveyByCareerMap.put(career, new HashMap<>());
                }

                if (!surveyByCareerMap.get(career).containsKey(survey.getTitle())) {
                    surveyByCareerMap.get(career).put(survey.getTitle(), new ArrayList<>());
                }

                surveyByCareerMap.get(career).get(survey.getTitle()).addAll(questionsWithAnswers);
            }
        }

        return ResponseEntity.ok(surveyByCareerMap);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/unanswered-surveys")
    public ResponseEntity<List<GraduadoWithUnansweredSurveysDTO>> getGraduadosWithUnansweredSurveys() {
        List<GraduadoWithUnansweredSurveysDTO> result = answerService.getGraduadosWithUnansweredSurveys();
        return ResponseEntity.ok(result);
    }

  

}
