package ec.edu.ista.springgc1.service.scheduled;

import ec.edu.ista.springgc1.model.dto.MailRequest;
import ec.edu.ista.springgc1.model.entity.Postulacion;
import ec.edu.ista.springgc1.model.enums.EstadoOferta;
import ec.edu.ista.springgc1.model.enums.EstadoPostulacion;
import ec.edu.ista.springgc1.repository.PostulacionRepository;
import ec.edu.ista.springgc1.service.impl.OfertaslaboralesServiceImpl;
import ec.edu.ista.springgc1.service.impl.PostulacionServiceImpl;
import ec.edu.ista.springgc1.service.mail.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ScheduledTaskExecutor {

    @Value("${spring.mail.username}")
    private String from;

    private final OfertaslaboralesServiceImpl ofertaslaboralesService;

    private final PostulacionRepository postulacionRepository;

    private final EmailService emailService;

    public ScheduledTaskExecutor(OfertaslaboralesServiceImpl ofertaslaboralesService, PostulacionRepository postulacionRepository, EmailService emailService) {
        this.ofertaslaboralesService = ofertaslaboralesService;
        this.postulacionRepository = postulacionRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void executeTask() {
        System.out.println("Ejecutando tarea programada");

        ofertaslaboralesService.findOfertasLaboralesWithOutEstadoFinalizado().forEach(oferta -> {

            if (oferta.getFechaCierre().isBefore(LocalDateTime.now())) {
                oferta.setEstado(EstadoOferta.EN_SELECCION);

                if (oferta.getFechaCierre().plusDays(3).isBefore(LocalDateTime.now())) {
                    oferta.setEstado(EstadoOferta.FINALIZADA);

                    List<Postulacion> postulacion =  postulacionRepository.findAllByOfertaLaboralId(oferta.getId()).stream().peek(g -> {
                        if (g.getEstado().equals(EstadoPostulacion.APLICANDO)) {
                            g.setEstado(EstadoPostulacion.RECHAZADO);
                        }
                    }).collect(Collectors.toList());

                    postulacionRepository.saveAll(postulacion);


                    Map<String, Object> model = new HashMap<>();

                    String[] emails = postulacion.stream().map(p -> p.getGraduado().getEmailPersonal()).toArray(String[]::new);

                    String subject = "Postulación rechazada";

                    String caseEmail = "reject-postulate";

                    model.put("oferta", oferta);

                    postulacionRepository.saveAll(postulacion);

                    MailRequest mailRequest = new MailRequest(from, subject, caseEmail);

                    emailService.sendEmail(mailRequest, model, emails);
                }

                ofertaslaboralesService.save(oferta);
            }
        });

        System.out.println("Tarea programada finalizada");
    }
}