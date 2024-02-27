package ec.edu.ista.springgc1.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ista.springgc1.model.dto.GraduadoDTO;
import ec.edu.ista.springgc1.model.dto.MailRequest;
import ec.edu.ista.springgc1.model.dto.MailResponse;
import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.model.entity.OfertasLaborales;
import ec.edu.ista.springgc1.service.impl.GraduadoServiceImpl;
import ec.edu.ista.springgc1.service.impl.OfertaslaboralesServiceImpl;
import ec.edu.ista.springgc1.service.mail.EmailService;

@RestController
@RequestMapping("/mail")
@CrossOrigin(origins = "http://localhost:4200")
public class EmailController {

	@Autowired
	private EmailService service;

	@Autowired
	private GraduadoServiceImpl graduadoService;
	
	@Autowired
	private OfertaslaboralesServiceImpl ofertaService;

	@PostMapping("/sendingEmail")
	public ResponseEntity<?> sendEmail(@RequestBody MailRequest request) {
		Graduado graduado = graduadoService.findByEmail(request.getTo());
		OfertasLaborales oferta = ofertaService.findById(Long.valueOf(request.getName()));
		
		String fullName = getFullName(graduado);
		
		Map<String, Object> model = new HashMap<>();
		model.put("fullName", fullName);
		model.put("graduado", graduado);
		model.put("oferta", oferta);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.sendEmail(request, model));
	}

	@PostMapping("/recovery-password")
	public ResponseEntity<?> recoveryPassword(@RequestBody MailRequest request) {
		Graduado graduado = graduadoService.findByEmail(request.getTo());
		
		String fullName = getFullName(graduado);
		
		Map<String, Object> model = new HashMap<>();
		model.put("fullName", fullName);
		model.put("graduado", graduado);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.sendRecoveryEmail(request, model));
	}
	
	private String getFullName(Graduado graduado) {
		return graduado.getUsuario().getPersona().getPrimer_nombre() 
				+ " " + graduado.getUsuario().getPersona().getSegundo_nombre()
				+ " " + graduado.getUsuario().getPersona().getApellido_paterno() 
				+ " " + graduado.getUsuario().getPersona().getApellido_materno();
	}
}
