package ec.edu.ista.springgc1.controller;

import java.util.List;
import java.util.Set;

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

import com.fasterxml.jackson.annotation.JsonView;

import ec.edu.ista.springgc1.model.dto.EmpresaDTO;
import ec.edu.ista.springgc1.model.entity.Empresa;
import ec.edu.ista.springgc1.service.impl.EmpresaServiceImpl;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/empresas")
public class EmpresaController {
	 @Autowired
	    private EmpresaServiceImpl empresaService;
	 @GetMapping
	 
	    ResponseEntity<List<?>> list() {
	        return ResponseEntity.ok(empresaService.findAll());
	    }

	    @GetMapping("/{id}")
	    ResponseEntity<?> findById(@PathVariable Long id) {
	        return ResponseEntity.ok(empresaService.findById(id));
	    }
	 @PostMapping
	    ResponseEntity<?> create(@Valid @RequestBody EmpresaDTO eDTO) {

	        return ResponseEntity.status(HttpStatus.CREATED)
	                .body(empresaService.save(eDTO));
	    }
	
	 @PutMapping("/{id}")
	    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody EmpresaDTO eDTO) {
	        // Obtener la empresa existente
	        Empresa existingEmpresa = empresaService.findById(id);
	        if (!existingEmpresa.getNombre().equals(eDTO.getNombre()) && empresaService.existsBynombre(eDTO.getNombre())) {
	            return ResponseEntity.status(HttpStatus.CONFLICT)
	                    .body("Ya existe otra empresa con el mismo nombre.");
	        }

	        // Actualizar la empresa
	        EmpresaDTO updatedEmpresa = empresaService.update(id, eDTO);
	        return ResponseEntity.ok(updatedEmpresa);
	    }
	 
	 
	 @DeleteMapping("/{id}")
	    ResponseEntity<?> delete(@PathVariable Long id) {
	            empresaService.delete(id);
	            return ResponseEntity.ok("Empresa eliminada exitosamente.");
	       
	    }
	 @GetMapping("/by-usuario/{nombreUsuario}")
	    ResponseEntity<?> findByNombreUsuario(@PathVariable String nombreUsuario) {
	        Set<EmpresaDTO> empresas = empresaService.findByNombreUsuario(nombreUsuario);
	        return ResponseEntity.ok(empresas);
	    }
}
