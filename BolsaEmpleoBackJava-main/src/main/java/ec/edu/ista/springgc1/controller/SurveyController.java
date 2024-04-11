package ec.edu.ista.springgc1.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.ista.springgc1.model.entity.Question;
import ec.edu.ista.springgc1.model.entity.Survey;
import ec.edu.ista.springgc1.service.impl.SurveyServiceImp;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    @Autowired
    private SurveyServiceImp surveyService;

   
    @PostMapping
    public ResponseEntity<Survey> saveOrUpdateSurvey(@RequestBody Survey survey) {
        Survey savedSurvey = surveyService.saveSurvey(survey);
        return new ResponseEntity<>(savedSurvey, HttpStatus.OK);
    }

   
    @GetMapping("/{surveyId}")
    public ResponseEntity<Survey> findSurveyById(@PathVariable Long surveyId) {
        Optional<Survey> survey = surveyService.findSurveyById(surveyId);
        return survey.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

  
    @DeleteMapping("/{surveyId}")
    public ResponseEntity<Void> deleteSurveyById(@PathVariable Long surveyId) {
        surveyService.deleteSurveyById(surveyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

 
    @PostMapping("/{surveyId}/questions")
    public ResponseEntity<Question> addQuestionToSurvey(@PathVariable Long surveyId, @RequestBody Question question) {
        Question savedQuestion = surveyService.addQuestionToSurvey(surveyId, question);
        return new ResponseEntity<>(savedQuestion, HttpStatus.OK);
    }

   
    @DeleteMapping("/{surveyId}/related")
    public ResponseEntity<Void> deleteSurveyAndRelated(@PathVariable Long surveyId) {
        surveyService.deleteSurveyAndRelated(surveyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/withQuestionsAndOptions")
    public ResponseEntity<?> getAllSurveysWithQuestionsAndOptions() {
        try {
            return ResponseEntity.ok(surveyService.getAllSurveysWithQuestionsAndOptions());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al recuperar las encuestas: " + e.getMessage());
        }
    }
    
    
    @PutMapping("/{surveyId}")
    public ResponseEntity<Survey> updateSurvey(@PathVariable Long surveyId, @RequestBody Survey updatedSurvey) {
        Optional<Survey> existingSurveyOptional = surveyService.findSurveyById(surveyId);

        if (existingSurveyOptional.isPresent()) {
            Survey existingSurvey = existingSurveyOptional.get();
            existingSurvey.setTitle(updatedSurvey.getTitle());
            existingSurvey.setDescription(updatedSurvey.getDescription());

            List<Question> existingQuestions = existingSurvey.getQuestions();
            List<Question> updatedQuestions = updatedSurvey.getQuestions();

            for (Question updatedQuestion : updatedQuestions) {
                Optional<Question> existingQuestionOptional = existingQuestions.stream()
                        .filter(q -> q.getId().equals(updatedQuestion.getId()))
                        .findFirst();

                if (existingQuestionOptional.isPresent()) {
                   
                    Question existingQuestion = existingQuestionOptional.get();
                    existingQuestion.setText(updatedQuestion.getText());
                    existingQuestion.setType(updatedQuestion.getType());
                    existingQuestion.setOptions(updatedQuestion.getOptions());
                } else {
                    updatedQuestion.setSurvey(existingSurvey); 
                    existingQuestions.add(updatedQuestion);
                }
            }

           
            try {
                Survey savedSurvey = surveyService.updateSurvey(existingSurvey);
                return ResponseEntity.ok(savedSurvey);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null); // Manejar errores
            }
        } else {
            return ResponseEntity.notFound().build(); // La encuesta no se encontró
        }
    }
  
}