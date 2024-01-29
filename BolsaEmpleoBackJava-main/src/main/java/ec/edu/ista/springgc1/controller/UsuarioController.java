package ec.edu.ista.springgc1.controller;

import ec.edu.ista.springgc1.model.dto.UsuarioDTO;
import ec.edu.ista.springgc1.model.entity.Usuario;
import ec.edu.ista.springgc1.service.impl.UsuarioServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.amazonaws.services.sns.model.ResourceNotFoundException;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @GetMapping
    public ResponseEntity<List<?>> list() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @GetMapping("/resumen/{id}")
    ResponseEntity<?> findByIdResumen(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findByIdToDTO(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {

        Usuario usuario = usuarioService.update(id, usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Usuario estudianteFromDb = usuarioService.findById(id);
        usuarioService.delete(estudianteFromDb.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-username/{username}")
    ResponseEntity<?> findByUsername(@PathVariable String username) {
        try {
            Usuario usuario = usuarioService.findByUsername2(username);
            return ResponseEntity.ok(usuario);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
