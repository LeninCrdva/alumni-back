package ec.edu.ista.springgc1.controller;

import ec.edu.ista.springgc1.model.entity.Question;
import ec.edu.ista.springgc1.service.impl.QuestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionServiceImpl questionService;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/all-questions")
    public ResponseEntity<?> getAllQuestions() {
        return ResponseEntity.ok(questionService.findAll());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GRADUADO', 'EMPRESARIO', 'RESPONSABLE_CARRERA')")
    @GetMapping("/all-questions-by-survey/{id}")
    public ResponseEntity<?> getAllQuestionsBySurvey(Long id) {
        return ResponseEntity.ok(questionService.findAllBySurveyId(id));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/create")
    public ResponseEntity<?> createQuestion(@RequestBody String question) {
        return ResponseEntity.ok(questionService.save(question));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable Long id, @RequestBody Question question) {
        Question questionToUpdate = questionService.findById(id);

        if (questionToUpdate == null) {
            return ResponseEntity.notFound().build();
        }

        questionToUpdate.setText(question.getText());
        questionToUpdate.setType(question.getType());
        questionToUpdate.setOptions(question.getOptions());

        return ResponseEntity.ok(questionService.save(questionToUpdate));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        questionService.delete(id);
        return ResponseEntity.ok().build();
    }
}
