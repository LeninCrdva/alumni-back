package ec.edu.ista.springgc1.service.impl;

import ec.edu.ista.springgc1.model.dto.GraduadoDTO;
import ec.edu.ista.springgc1.model.dto.MailRequest;
import ec.edu.ista.springgc1.model.dto.OfertasLaboralesDTO;
import ec.edu.ista.springgc1.model.dto.PostulacionDto;
import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.model.entity.OfertasLaborales;
import ec.edu.ista.springgc1.model.entity.Postulacion;
import ec.edu.ista.springgc1.model.entity.Usuario;
import ec.edu.ista.springgc1.model.enums.EstadoPostulacion;
import ec.edu.ista.springgc1.repository.PostulacionRepository;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.mail.EmailService;
import ec.edu.ista.springgc1.service.map.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostulacionServiceImpl extends GenericServiceImpl<Postulacion> implements Mapper<Postulacion, PostulacionDto> {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PostulacionRepository postulacionRepository;

    @Autowired
    private GraduadoServiceImpl graduadoService;

    @Autowired
    private OfertaslaboralesServiceImpl ofertasLaboralesService;

    public Integer countPostulacionByFechaPostulacionIsStartingWithOrderBy(LocalDate fechaPostulacion) {
        return postulacionRepository.countPostulacionByFechaPostulacionIsStartingWith(fechaPostulacion);
    }

    public List<Postulacion> findGraduadosByOfertaLaboralId(Long ofertaLaboralId) {
        return postulacionRepository.findAllByOfertaLaboralId(ofertaLaboralId);
    }

    public List<Postulacion> findOfertasLaboralesByGraduadoId(Long graduadoId) {
        return postulacionRepository.findAllByGraduadoUsuarioId(graduadoId);
    }

    public List<Postulacion> findGraduadosSinPostulacion() {
        return postulacionRepository.findGraduadosSinPostulacion();
    }

    public Postulacion savePostulacion(PostulacionDto postulacionDto) {
        Postulacion postulacion = mapToEntity(postulacionDto);

        sendEmail(postulacion);

        return postulacionRepository.save(postulacion);
    }

    public Postulacion updateEstado(Long id, PostulacionDto postulacionDto) {
        Postulacion postulacion = postulacionRepository.findById(id).get();
        postulacion.setEstado(postulacionDto.getEstado());

        sendEmail(postulacion);

        return postulacionRepository.save(postulacion);
    }

    @Override
    public Postulacion mapToEntity(PostulacionDto postulacionDto) {
        Postulacion postulacion = new Postulacion();
        Graduado graduado = graduadoService.findByIdUsuario(postulacionDto.getGraduado());
        OfertasLaborales ofertaLaboral = ofertasLaboralesService.findById(postulacionDto.getOfertaLaboral());

        postulacion.setId(postulacionDto.getId());
        postulacion.setGraduado(graduado);
        postulacion.setOfertaLaboral(ofertaLaboral);
        postulacion.setEstado(postulacionDto.getEstado());

        return postulacion;
    }

    @Override
    public PostulacionDto mapToDTO(Postulacion postulacion) {
        PostulacionDto postulacionDto = new PostulacionDto();

        postulacionDto.setId(postulacion.getId());
        postulacionDto.setGraduado(postulacion.getGraduado().getId());
        postulacionDto.setOfertaLaboral(postulacion.getOfertaLaboral().getId());
        postulacionDto.setEstado(postulacion.getEstado());

        return postulacionDto;
    }

    private void sendEmail(Postulacion postulacion) {
        Map<String, Object> model = new HashMap<>();

        createRequestEmail(postulacion, model);
    }

    private void createRequestEmail(Postulacion postulacion, Map<String, Object> model) {
        Graduado graduado = postulacion.getGraduado();
        OfertasLaborales oferta = postulacion.getOfertaLaboral();

        String fullName = getFullName(graduado.getUsuario());

        model.put("fullName", fullName);
        model.put("graduado", graduado);
        model.put("oferta", oferta);

        MailRequest request = createMailRequest(graduado, fullName, getMailCase(postulacion.getEstado().name()));

        emailService.sendEmail(request, model);
    }

    private String getFullName(Usuario usuario) {

        return usuario.getPersona().getPrimer_nombre()
                + " " + usuario.getPersona().getSegundo_nombre()
                + " " + usuario.getPersona().getApellido_paterno()
                + " " + usuario.getPersona().getApellido_materno();
    }

    private MailRequest createMailRequest(Graduado graduado, String fullName, String mailCase) {
        return new MailRequest(fullName, graduado.getEmailPersonal(), from, "¡Haz realizado una postulación!", mailCase);
    }

    private String getMailCase(String mailCase) {
        switch (mailCase) {
            case "APLICANDO":
                return "postulate";
            case "CANCELADA_POR_GRADUADO":
            case "CANCELADA_POR_ADMINISTRADOR":
                return "remove-postulate";
            default:
                return "¡Not have a case!";
        }
    }
}
