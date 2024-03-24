package ec.edu.ista.springgc1.controller;

import ec.edu.ista.springgc1.model.entity.Option;
import ec.edu.ista.springgc1.service.impl.OptionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/options")
public class OptionControlller {

    @Autowired
    private OptionServiceImpl optionService;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/all-options")
    public ResponseEntity<?> getAllOptions() {
        return ResponseEntity.ok(optionService.findAll());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/all-options-by-question/{id}")
    public ResponseEntity<?> getAllOptionsByQuestion(Long id) {
        return ResponseEntity.ok(optionService.findByQuestionId(id));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/create")
    public ResponseEntity<?> createOption(@RequestBody Option option) {
        return ResponseEntity.ok(optionService.save(option));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateOption(@PathVariable Long id, @RequestBody Option option) {
        Option optionToUpdate = optionService.findById(id);

        if (optionToUpdate == null) {
            return ResponseEntity.notFound().build();
        }

        optionToUpdate.setTitle(option.getTitle());
        optionToUpdate.setQuestion(option.getQuestion());

        return ResponseEntity.ok(optionService.save(optionToUpdate));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOption(@PathVariable Long id) {
        optionService.delete(id);
        return ResponseEntity.ok().build();
    }
}
