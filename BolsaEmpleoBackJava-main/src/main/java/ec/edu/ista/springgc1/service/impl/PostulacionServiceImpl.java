package ec.edu.ista.springgc1.service.impl;

import ec.edu.ista.springgc1.exception.AppException;
import ec.edu.ista.springgc1.model.dto.MailRequest;
import ec.edu.ista.springgc1.model.dto.PostulacionDto;
import ec.edu.ista.springgc1.model.entity.*;
import ec.edu.ista.springgc1.repository.*;
import ec.edu.ista.springgc1.service.generatorpdf.PDFGeneratorService;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.mail.EmailService;
import ec.edu.ista.springgc1.service.map.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @Autowired
    private CapacitacionRepository capacitacionRepository;

    @Autowired
    private ExperienciaRepository experienciaRepository;

    @Autowired
    private LogroRepository logroRepository;

    @Autowired
    private ReferenciaPersonalRepository referenciaPersonalRepository;

    @Autowired
    private ReferenciaProfesionalRepository referenciaProfesionalRepository;

    @Autowired
    private TituloRepository tituloRepository;

    private final PDFGeneratorService pdfGeneratorService;

    public PostulacionServiceImpl(PDFGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }

    public Integer countPostulacionByFechaPostulacionIsStartingWithOrderBy(LocalDateTime fechaPostulacion) {
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

    public Postulacion savePostulacion(PostulacionDto postulacionDto) throws IOException {
        Postulacion postulacion = mapToEntity(postulacionDto);
        Map<String, Object> model = getPreviousRequest(postulacion.getGraduado());

        if (((List<?>) model.get("titulos")).isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST,"No se puede postular sin tener al menos un título registrado.");
        }

        if (((List<?>) model.get("referencias")).isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "No se puede postular sin tener al menos una referencia personal registrada.");
        }

        if (((List<?>) model.get("referenciasProfesionales")).isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "No se puede postular sin tener al menos una referencia profesional registrada.");
        }

        byte[] pdfBytes = getAllDateForCurriculum(model);

        sendEmailWithPDF(postulacion, pdfBytes);

        sendEmailWithOutPDF(postulacion);

        return postulacionRepository.save(postulacion);
    }

    public Postulacion updateEstado(Long id, PostulacionDto postulacionDto) {
        Postulacion postulacion = postulacionRepository.findById(id).get();
        postulacion.setEstado(postulacionDto.getEstado());

        sendEmailWithOutPDF(postulacion);

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

    private byte[] getAllDateForCurriculum(Map<String, Object> model) throws IOException {

        return pdfGeneratorService.generatePDF(model);
    }

    private Map<String, Object> getPreviousRequest(Graduado graduado) {
        String fullName = getFullName(graduado.getUsuario());
        List<Experiencia> experiencias = experienciaRepository.findAllByCedulaGraduado_Id(graduado.getId());
        List<Logro> logros = logroRepository.findAllByGraduadoId(graduado.getId());
        List<Capacitacion> capacitaciones = capacitacionRepository.findAllByGraduadoId(graduado.getId());
        List<Referencia_Personal> referencias = referenciaPersonalRepository.findAllByGraduadoId(graduado.getId());
        List<ReferenciaProfesional> referenciasProfesionales = referenciaProfesionalRepository.findAllByGraduadoId(graduado.getId());
        List<Titulo> titulos = tituloRepository.findAllByGraduadoId(graduado.getId());

        Map<String, Object> model = new HashMap<>();
        model.put("fullName", fullName);
        model.put("graduado", graduado);
        model.put("experiencias", experiencias);
        model.put("logros", logros);
        model.put("capacitaciones", capacitaciones);
        model.put("referencias", referencias);
        model.put("referenciasProfesionales", referenciasProfesionales);
        model.put("titulos", titulos);

        return model;
    }

    private void sendEmailWithPDF(Postulacion postulacion, byte[] pdfBytes) {
        Map<String, Object> model = new HashMap<>();

        createRequestAndSendEmailWithPDF(postulacion, model, pdfBytes);
    }

    private void sendEmailWithOutPDF(Postulacion postulacion) {
        Map<String, Object> model = new HashMap<>();

        createRequestAndSendEmailWithOutPDF(postulacion, model);
    }

    private void createRequestAndSendEmailWithPDF(Postulacion postulacion, Map<String, Object> model, byte[] pdfBytes) {
        Graduado graduado = postulacion.getGraduado();
        OfertasLaborales oferta = postulacion.getOfertaLaboral();

        String fullName = getFullName(oferta.getEmpresa().getEmpresario().getUsuario());
        String fullNameGraduado = getFullName(graduado.getUsuario());

        model.put("fullName", fullName);
        model.put("fullNameGraduate", fullNameGraduado);
        model.put("url", graduado.getUsuario().getRutaImagen());
        model.put("graduado", graduado);
        model.put("oferta", oferta);

        String email = oferta.getEmpresa().getEmpresario().getEmail();
        String mailCase = "cv-graduate";

        MailRequest request = createMailRequest(email, fullName, mailCase);

        emailService.sendEmailWithPDF(request, model, pdfBytes);
    }

    private void createRequestAndSendEmailWithOutPDF(Postulacion postulacion, Map<String, Object> model) {
        Graduado graduado = postulacion.getGraduado();
        OfertasLaborales oferta = postulacion.getOfertaLaboral();

        String fullName = getFullName(graduado.getUsuario());

        model.put("fullName", fullName);
        model.put("graduado", graduado);
        model.put("oferta", oferta);

        String mailCase = getMailCase(postulacion.getEstado().name());

        MailRequest request = createMailRequest(graduado.getEmailPersonal(), fullName, mailCase);

        emailService.sendEmail(request, model);
    }

    private String getFullName(Usuario usuario) {

        return usuario.getPersona().getPrimerNombre()
                + " " + usuario.getPersona().getSegundoNombre()
                + " " + usuario.getPersona().getApellidoPaterno()
                + " " + usuario.getPersona().getApellidoMaterno();
    }

    private MailRequest createMailRequest(String email, String fullName, String mailCase) {
        return new MailRequest(fullName, email, from, getSubject(mailCase), mailCase);
    }

    private String getSubject(String mailCase) {
        switch (mailCase) {
            case "postulate":
                return "¡Haz realizado una postulación!";
            case "remove-postulate":
                return "¡Tu postulación se ha cancelado!";
            case "cv-graduate":
                return "¡Ha recibido un interesado en la oferta laboral!";
            default:
                return "¡Not have a case!";
        }
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
