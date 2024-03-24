package ec.edu.ista.springgc1.controller;

import ec.edu.ista.springgc1.model.entity.Survey;
import ec.edu.ista.springgc1.service.impl.SurveyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/surveys")
public class SurveyController {

    @Autowired
    private SurveyServiceImpl surveyService;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllSurveys() {
        return ResponseEntity.ok(surveyService.findAll());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GRADUADO')")
    @GetMapping("/find-survey/{id}")
    public ResponseEntity<?> getSurveyById(Long id) {
        return ResponseEntity.ok(surveyService.findById(id));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/create")
    public ResponseEntity<?> createSurvey(@RequestBody Survey survey) {
        return ResponseEntity.ok(surveyService.save(survey));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSurvey(@PathVariable Long id, @RequestBody Survey survey) {
        Survey surveyToUpdate = surveyService.findById(id);

        if (surveyToUpdate == null) {
            return ResponseEntity.notFound().build();
        }

        surveyToUpdate.setTitle(survey.getTitle());
        surveyToUpdate.setDescription(survey.getDescription());

        return ResponseEntity.ok(surveyService.save(surveyToUpdate));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSurvey(@PathVariable Long id) {
        surveyService.delete(id);
        return ResponseEntity.ok().build();
    }
}
