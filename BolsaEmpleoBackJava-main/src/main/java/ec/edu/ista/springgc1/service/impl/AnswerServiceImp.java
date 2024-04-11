package ec.edu.ista.springgc1.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.edu.ista.springgc1.model.dto.AnswerSearchDTO;

import ec.edu.ista.springgc1.model.dto.QuestionWithAnswersDTO;
import ec.edu.ista.springgc1.model.dto.SurveyQuestionsAnswersDTO;
import ec.edu.ista.springgc1.model.entity.Answer;
import ec.edu.ista.springgc1.model.entity.Carrera;
import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.model.entity.Question;
import ec.edu.ista.springgc1.model.entity.Survey;
import ec.edu.ista.springgc1.repository.AnswerRepository;
import ec.edu.ista.springgc1.repository.CarreraRepository;
import ec.edu.ista.springgc1.repository.GraduadoRepository;
import ec.edu.ista.springgc1.repository.SurveyRepository;

@Service
@Transactional
public class AnswerServiceImp {
	  @Autowired
	    private AnswerRepository answerRepository;

	    @Autowired
	    private GraduadoRepository graduadoRepository;

	    @Autowired
	    private CarreraRepository carreraRepository;
	    @Autowired
	    private SurveyRepository surveyRepository;

	    public Answer saveAnswer(AnswerSearchDTO answerDTO) {
	       
	        Optional<Graduado> optionalGraduado = graduadoRepository.findByEmailPersonal(answerDTO.getGraduadoEmail());
	        if (!optionalGraduado.isPresent()) {
	            throw new IllegalArgumentException("Graduado no encontrado con el email: " + answerDTO.getGraduadoEmail());
	        }
	        Graduado graduado = optionalGraduado.get();

	        Optional<Carrera> optionalCarrera = carreraRepository.findByNombre(answerDTO.getCarreraNombre());
	        if (!optionalCarrera.isPresent()) {
	            throw new IllegalArgumentException("Carrera no encontrada con el nombre: " + answerDTO.getCarreraNombre());
	        }
	        Carrera carrera = optionalCarrera.get();  
	        Optional<Survey> optionalSurvey = surveyRepository.findByTitle(answerDTO.getSurveyTitle());
	        if (!optionalSurvey.isPresent()) {
	            throw new IllegalArgumentException("Encuesta no encontrada con el título: " + answerDTO.getSurveyTitle());
	        }
	        Survey survey = optionalSurvey.get();

	        Answer answer = new Answer();
	        answer.setGraduado(graduado);
	        answer.setCarrera(carrera);
	        answer.setSurvey(survey);
	        answer.setOpenAnswer(answerDTO.getOpenAnswer());

	       
	        if (answerDTO.getQuestionResponses() != null && !answerDTO.getQuestionResponses().isEmpty()) {
	            for (Map.Entry<Long, String> entry : answerDTO.getQuestionResponses().entrySet()) {
	                answer.assignAnswerToQuestion(entry.getKey(), entry.getValue());
	            }
	        }

	     
	        return answerRepository.save(answer);
	    }
	    //listar preguntas con respuestas por encuesta por id sirvery
	    public Map<Question, List<Answer>> loadQuestionsWithAnswersBySurveyId(Long surveyId) {
	        Optional<Survey> optionalSurvey = surveyRepository.findById(surveyId);
	        if (optionalSurvey.isPresent()) {
	            Survey survey = optionalSurvey.get();
	            List<Question> questions = survey.getQuestions();
	            List<Answer> answers = answerRepository.findBySurveyId(surveyId);

	            Map<Question, List<Answer>> questionAnswersMap = new HashMap<>();

	            for (Question question : questions) {
	                List<Answer> questionAnswers = new ArrayList<>();
	                for (Answer answer : answers) {
	                    if (answer.getAnswers().containsKey(question.getId())) {
	                        questionAnswers.add(answer);
	                    }
	                }
	                questionAnswersMap.put(question, questionAnswers);
	            }

	            return questionAnswersMap;
	        } else {
	            throw new IllegalArgumentException("No se encontró la encuesta con ID: " + surveyId);
	        }
	    }



	    // Método auxiliar para obtener las respuestas de una pregunta específica
	    private List<Answer> getAnswersForQuestion(Long questionId, List<Answer> allAnswers) {
	        List<Answer> questionAnswers = new ArrayList<>();
	        for (Answer answer : allAnswers) {
	            if (answer.getAnswers().containsKey(questionId)) {
	                questionAnswers.add(answer);
	            }
	        }
	        return questionAnswers;
	    }

	    
	    //All
	    
	 // Método para listar todas las encuestas con preguntas y respuestas asociadas
	    public List<SurveyQuestionsAnswersDTO> loadAllSurveysWithQuestionsAndAnswers() {
	        List<Survey> allSurveys = surveyRepository.findAll();
	        List<SurveyQuestionsAnswersDTO> surveyQuestionsAnswersList = new ArrayList<>();

	        for (Survey survey : allSurveys) {
	            SurveyQuestionsAnswersDTO surveyDTO = new SurveyQuestionsAnswersDTO();
	            surveyDTO.setSurveyId(survey.getId());
	            surveyDTO.setSurveyTitle(survey.getTitle());
	            surveyDTO.setSurveyDescription(survey.getDescription());

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

	            surveyDTO.setQuestionsWithAnswers(questionsWithAnswers);
	            surveyQuestionsAnswersList.add(surveyDTO);
	        }

	        return surveyQuestionsAnswersList;
	    }


	   
	   
}
