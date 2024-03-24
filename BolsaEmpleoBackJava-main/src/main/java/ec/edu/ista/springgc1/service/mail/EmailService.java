package ec.edu.ista.springgc1.service.mail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import ec.edu.ista.springgc1.mail.config.RecoveryPasswordToken;
import ec.edu.ista.springgc1.model.dto.MailRequest;
import ec.edu.ista.springgc1.model.dto.MailResponse;
import ec.edu.ista.springgc1.model.entity.RecoveryToken;
import ec.edu.ista.springgc1.model.entity.Usuario;
import ec.edu.ista.springgc1.service.impl.AdministradorServiceImpl;
import ec.edu.ista.springgc1.service.impl.GraduadoServiceImpl;
import ec.edu.ista.springgc1.service.impl.RecoveryTokenServiceImpl;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private ITemplateEngine templateEngine;

    @Autowired
    private RecoveryTokenServiceImpl tokenService;

    @Autowired
    private GraduadoServiceImpl graduadoService;

    @Autowired
    private AdministradorServiceImpl graduAdministradorServiceImpl;

    @Autowired
    private RecoveryPasswordToken tokenPasswordRecovery;

    @Value("${angular.recovery.url}")
    private String RECOVERY_URL;

    public MailResponse sendEmail(MailRequest request, Map<String, Object> model) {
        MailResponse response = new MailResponse();
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            getHtml(request, model, response, message, helper);
        } catch (MessagingException | IOException e) {
            response.setMessage("Fallo al enviar email: " + e.getMessage());
            response.setStatus(Boolean.FALSE);
        }

        return response;
    }

    public MailResponse sendRecoveryEmail(MailRequest request, Map<String, Object> model) {
        MailResponse response = new MailResponse();
        MimeMessage message = sender.createMimeMessage();
        RecoveryToken token = generateRecoveryToken(request.getTo());
        String activationUrl = RECOVERY_URL + "?reset_token=" + token.getToken();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            model.put("URL", activationUrl);

            getHtml(request, model, response, message, helper);

        } catch (MessagingException | IOException e) {
            response.setMessage("Fallo al enviar email: " + e.getMessage());
            response.setStatus(Boolean.FALSE);
        }

        return response;
    }

    private void getHtml(MailRequest request, Map<String, Object> model, MailResponse response, MimeMessage message, MimeMessageHelper helper) throws IOException, MessagingException {
        Context context = new Context();
        context.setVariables(model);

        String html = templateEngine.process(getTemplate(request.getCaseEmail()), context);

        helper.setTo(request.getTo());
        helper.setText(html, true);
        helper.setSubject(request.getSubject());
        helper.setFrom(request.getFrom());
        sender.send(message);

        response.setMessage("Email enviado a: " + request.getTo());
        response.setStatus(Boolean.TRUE);
    }

    private static String getTemplate(String emailCase) {
        String t;

        switch (emailCase) {
            case "postulate":
                t = "graduate/email-template-postulate";
                break;
            case "remove-postulate":
                t = "graduate/email-template-remove-postulate";
                break;
            case "accept-postulate":
                t = "graduate/email-template-accept-postulate";
                break;
            case "reset-password":
                t = "graduate/email-template-recovery-email";
                break;
            case "list-postulates":
                t = "businessman/email-template-list-postulates";
                break;
            default:
                t = "graduate/email-template-contact-us";
                break;
        }

        return t;
    }

    private RecoveryToken generateRecoveryToken(String email) {
        String tokenValue = tokenPasswordRecovery.generateToken();
        RecoveryToken recoveryToken = new RecoveryToken();
        Usuario usuario = graduadoService.findByEmail(email).getUsuario();
        Date expirationDate = tokenPasswordRecovery.calculateExpirationDate();

        if (usuario != null && !usuario.getNombreUsuario().isEmpty()) {
            recoveryToken.setUsuario(usuario);
        } else {
            usuario = graduAdministradorServiceImpl.findByEmail(email).getUsuario();
            recoveryToken.setUsuario(usuario);
        }

        recoveryToken.setToken(tokenValue);
        recoveryToken.setExpiration(expirationDate);

        return tokenService.save(recoveryToken);
    }
}
