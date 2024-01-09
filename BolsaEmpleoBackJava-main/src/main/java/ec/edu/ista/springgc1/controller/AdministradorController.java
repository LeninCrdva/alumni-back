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

import ec.edu.ista.springgc1.model.dto.AdminDTO;
import ec.edu.ista.springgc1.model.entity.Administrador;
import ec.edu.ista.springgc1.service.impl.AdministradorServiceImpl;
import ec.edu.ista.springgc1.service.impl.SuperAdminServiceImpl;

@CrossOrigin
@RestController
@RequestMapping("/administradores")
public class AdministradorController {

	@Autowired
    private AdministradorServiceImpl adminService;

    @GetMapping
    ResponseEntity<List<?>> list() {
        return ResponseEntity.ok(adminService.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.findById(id));
    }


    @GetMapping("/usuario/{id}")
    ResponseEntity<?> findByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.findByUsuario(id));
    }

   
    @PostMapping
    ResponseEntity<?> create(@Valid @RequestBody AdminDTO adminDTO) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminService.save(adminDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody AdminDTO adminDTO) {
    	 AdminDTO adminFromDb = adminService.findByIdToDTO(id);       
    	 adminFromDb.setUsuario(adminDTO.getUsuario());
    	 adminFromDb.setEstado(adminDTO.isEstado());
    	 adminFromDb.setCargo(adminDTO.getCargo());
    	 adminFromDb.setEmail(adminDTO.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.save(adminFromDb));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
    	Administrador adminFromDb = adminService.findById(id);
    	adminService.delete(adminFromDb.getId());
        return ResponseEntity.noContent().build();
    }
}
