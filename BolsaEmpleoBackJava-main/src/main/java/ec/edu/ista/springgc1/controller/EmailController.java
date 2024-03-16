package ec.edu.ista.springgc1.controller;

import java.util.HashMap;
import java.util.Map;

import ec.edu.ista.springgc1.model.entity.*;
import ec.edu.ista.springgc1.service.impl.AdministradorServiceImpl;
import ec.edu.ista.springgc1.service.impl.EmpresarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ista.springgc1.model.dto.GraduadoDTO;
import ec.edu.ista.springgc1.model.dto.MailRequest;
import ec.edu.ista.springgc1.model.dto.MailResponse;
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
    private AdministradorServiceImpl administradorService;

    @Autowired
    private EmpresarioServiceImpl empresarioService;

    @Autowired
    private OfertaslaboralesServiceImpl ofertaService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GRADUADO', 'EMPRESARIO')")
    @PostMapping("/sendingEmail")
    public ResponseEntity<?> sendEmail(@RequestBody MailRequest request) {
        Graduado graduado = graduadoService.findByEmail(request.getTo());
        OfertasLaborales oferta = ofertaService.findById(Long.valueOf(request.getName()));

        String fullName = getFullName(graduado.getUsuario());

        Map<String, Object> model = new HashMap<>();
        model.put("fullName", fullName);
        model.put("graduado", graduado);
        model.put("oferta", oferta);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.sendEmail(request, model));
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/recovery-password")
    public ResponseEntity<?> recoveryPassword(@RequestBody MailRequest request) {
        Graduado graduado = graduadoService.findByEmail(request.getTo());
        Empresario empresario = StringUtils.hasText(graduado.getEmailPersonal()) ? empresarioService.findByEmail(request.getTo()) : new Empresario();
        Administrador administrador = (!StringUtils.hasText(graduado.getEmailPersonal()) && !StringUtils.hasText(empresario.getEmail())) ? administradorService.findByEmail(request.getTo()) : new Administrador();

        if (StringUtils.hasText(graduado.getEmailPersonal())) {
            return createResponse(graduado.getUsuario(), request);
        } else if (StringUtils.hasText(empresario.getEmail())) {
            return createResponse(empresario.getUsuario(), request);
        } else if (StringUtils.hasText(administrador.getEmail())) {
            return createResponse(administrador.getUsuario(), request);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/contact-us")
    public ResponseEntity<MailResponse> contactUs(@RequestBody MailRequest request) {

        Map<String, Object> model = new HashMap<>();
        model.put("fullName", request.getName());
        model.put("email", request.getFrom());
        model.put("message", request.getCaseEmail());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.sendEmail(request, model));
    }

    private String getFullName(Usuario usuario) {

        return usuario.getPersona().getPrimer_nombre()
                + " " + usuario.getPersona().getSegundo_nombre()
                + " " + usuario.getPersona().getApellido_paterno()
                + " " + usuario.getPersona().getApellido_materno();
    }

    private ResponseEntity<?> createResponse(Usuario usuario, MailRequest request) {
        Map<String, Object> model = new HashMap<>();
        model.put("fullName", getFullName(usuario));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.sendRecoveryEmail(request, model));
    }
}
