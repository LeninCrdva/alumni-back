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

import ec.edu.ista.springgc1.model.dto.SuperAdminDTO;
import ec.edu.ista.springgc1.model.entity.SuperAdmin;
import ec.edu.ista.springgc1.service.impl.SuperAdminServiceImpl;


@CrossOrigin
@RestController
@RequestMapping("/superadmin")
public class SuperAdminController {

	@Autowired
    private SuperAdminServiceImpl superadminService;

    @GetMapping
    ResponseEntity<List<?>> list() {
        return ResponseEntity.ok(superadminService.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(superadminService.findById(id));
    }


    @GetMapping("/usuario/{id}")
    ResponseEntity<?> findByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(superadminService.findByUsuario(id));
    }

   
    @PostMapping
    ResponseEntity<?> create(@Valid @RequestBody SuperAdminDTO superadminDTO) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(superadminService.save(superadminDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody SuperAdminDTO superadminDTO) {
    	 SuperAdminDTO superadminFromDb = superadminService.findByIdToDTO(id);       
    	 superadminFromDb.setUsuario(superadminDTO.getUsuario());
    	 superadminFromDb.setEstado(superadminDTO.isEstado());

        return ResponseEntity.status(HttpStatus.CREATED).body(superadminService.save(superadminFromDb));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
    	SuperAdmin superadminFromDb = superadminService.findById(id);
    	superadminService.delete(superadminFromDb.getId());
        return ResponseEntity.noContent().build();
    }

}
