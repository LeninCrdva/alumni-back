package ec.edu.ista.springgc1.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ista.springgc1.model.dto.EmpresarioDTO;
import ec.edu.ista.springgc1.model.entity.Empresario;
import ec.edu.ista.springgc1.service.impl.EmpresarioServiceImpl;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/empresarios")
public class EmpresarioController {
	@Autowired
    private EmpresarioServiceImpl emprendimientoService;

    @GetMapping
    ResponseEntity<List<?>> list() {
        return ResponseEntity.ok(emprendimientoService.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(emprendimientoService.findById(id));
    }


    @GetMapping("/usuario/{id}")
    ResponseEntity<?> findByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(emprendimientoService.findByUsuario(id));
    }

   
    @PostMapping
    ResponseEntity<?> create(@Valid @RequestBody EmpresarioDTO empresarioDTO) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(emprendimientoService.save(empresarioDTO));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody EmpresarioDTO empresarioDTO) {
        EmpresarioDTO updatedEmpresario = emprendimientoService.update(id, empresarioDTO);
        return ResponseEntity.ok(updatedEmpresario);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
    	emprendimientoService.delete(id);
            return ResponseEntity.ok("Empresa eliminada exitosamente.");
       
    }

}
