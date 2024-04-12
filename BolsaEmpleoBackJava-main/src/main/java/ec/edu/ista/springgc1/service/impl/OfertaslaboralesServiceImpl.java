package ec.edu.ista.springgc1.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import ec.edu.ista.springgc1.exception.AppException;
import ec.edu.ista.springgc1.model.dto.MailRequest;
import ec.edu.ista.springgc1.model.entity.*;
import ec.edu.ista.springgc1.model.enums.EstadoOferta;
import ec.edu.ista.springgc1.model.enums.EstadoPostulacion;
import ec.edu.ista.springgc1.repository.*;
import ec.edu.ista.springgc1.service.bucket.S3Service;
import ec.edu.ista.springgc1.service.generatorpdf.ImageOptimizer;
import ec.edu.ista.springgc1.service.mail.EmailService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ec.edu.ista.springgc1.exception.ResourceNotFoundException;
import ec.edu.ista.springgc1.model.dto.OfertasLaboralesDTO;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.map.Mapper;
import org.springframework.util.StringUtils;

@Service
public class OfertaslaboralesServiceImpl extends GenericServiceImpl<OfertasLaborales> implements Mapper<OfertasLaborales, OfertasLaboralesDTO> {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private OfertaslaboralesRepository ofertasLaboralesRepository;

    @Autowired
    private ContratacionRepository contratacionRepository;

    @Autowired
    private EmpresaRepository empresarepository;

    @Autowired
    private GraduadoRepository graduadoRepository;

    @Autowired
    private PostulacionRepository postulacionRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ImageOptimizer imageOptimizer;

    @Override
    public OfertasLaborales mapToEntity(OfertasLaboralesDTO dto) {
        OfertasLaborales ofertaLaboral = new OfertasLaborales();
        ofertaLaboral.setId(dto.getId());
        ofertaLaboral.setSalario(dto.getSalario());
        ofertaLaboral.setFechaCierre(dto.getFechaCierre());
        ofertaLaboral.setFechaApertura(dto.getFechaApertura());
        ofertaLaboral.setCargo(dto.getCargo());
        ofertaLaboral.setExperiencia(dto.getExperiencia());
        ofertaLaboral.setFechaPublicacion(dto.getFechaPublicacion());
        ofertaLaboral.setAreaConocimiento(dto.getAreaConocimiento());
        ofertaLaboral.setEstado(dto.getEstado());
        Empresa emp = empresarepository.findByNombre(dto.getNombreEmpresa())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", dto.getNombreEmpresa()));

        ofertaLaboral.setEmpresa(emp);
        ofertaLaboral.setTipo(dto.getTipo());
        ofertaLaboral.setFotoPortada(dto.getFotoPortada());
        ofertaLaboral.setTiempo(dto.getTiempo());

        return ofertaLaboral;
    }

    @Override
    public OfertasLaboralesDTO mapToDTO(OfertasLaborales entity) {
        OfertasLaboralesDTO dto = new OfertasLaboralesDTO();
        dto.setId(entity.getId());
        dto.setSalario(entity.getSalario());
        dto.setFechaCierre(entity.getFechaCierre());
        dto.setFechaPublicacion(entity.getFechaPublicacion());
        dto.setCargo(entity.getCargo());
        dto.setExperiencia(entity.getExperiencia());
        dto.setFechaApertura(entity.getFechaApertura());
        dto.setAreaConocimiento(entity.getAreaConocimiento());
        dto.setEstado(entity.getEstado());
        dto.setNombreEmpresa(entity.getEmpresa().getNombre());
        dto.setTipo(entity.getTipo());
        dto.setFotoPortada(entity.getFotoPortada());
        dto.setTiempo(entity.getTiempo());

        return dto;
    }

    @Override
    public List<OfertasLaboralesDTO> findAll() {
        return ofertasLaboralesRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<OfertasLaboralesDTO> findOfertasLaboralesWithOutEstadoFinalizado() {
        return ofertasLaboralesRepository.findOfertasLaboralesWithOutEstadoFinalizado().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public OfertasLaborales save(Object entity) {
        OfertasLaborales ofertaLaboral = ofertasLaboralesRepository.save(mapToEntity((OfertasLaboralesDTO) entity));

        createRequestAndSendEmailWithPDF(ofertaLaboral);

        return ofertaLaboral;
    }

    public OfertasLaboralesDTO update(Long id, OfertasLaboralesDTO updatedOfertaLaboralDTO) {
        OfertasLaborales existingOfertaLaboral = ofertasLaboralesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OfertaLaboral", String.valueOf(id)));

        if (existingOfertaLaboral.getEstado().equals(EstadoOferta.CANCELADA)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "No se puede actualizar una oferta laboral que ha sido cancelada");
        }

        if (existingOfertaLaboral.getEstado().equals(EstadoOferta.FINALIZADA)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "No se puede actualizar una oferta laboral que ha sido finalizada");
        }

        existingOfertaLaboral.setSalario(ObjectUtils.isEmpty(updatedOfertaLaboralDTO.getSalario()) ? existingOfertaLaboral.getSalario() : updatedOfertaLaboralDTO.getSalario());
        existingOfertaLaboral.setFechaCierre(ObjectUtils.isEmpty(updatedOfertaLaboralDTO.getFechaCierre()) ? existingOfertaLaboral.getFechaCierre() : updatedOfertaLaboralDTO.getFechaCierre());
        existingOfertaLaboral.setFechaApertura(ObjectUtils.isEmpty(updatedOfertaLaboralDTO.getFechaApertura()) ?  existingOfertaLaboral.getFechaApertura() : updatedOfertaLaboralDTO.getFechaApertura());
        existingOfertaLaboral.setCargo(updatedOfertaLaboralDTO.getCargo());
        existingOfertaLaboral.setExperiencia(updatedOfertaLaboralDTO.getExperiencia());
        existingOfertaLaboral.setAreaConocimiento(updatedOfertaLaboralDTO.getAreaConocimiento());
        existingOfertaLaboral.setFotoPortada(updatedOfertaLaboralDTO.getFotoPortada());
        existingOfertaLaboral.setTipo(updatedOfertaLaboralDTO.getTipo());
        existingOfertaLaboral.setTiempo(ObjectUtils.isEmpty(updatedOfertaLaboralDTO.getTiempo()) ? existingOfertaLaboral.getTiempo() : updatedOfertaLaboralDTO.getTiempo());
        Empresa empresa = empresarepository.findByNombre(updatedOfertaLaboralDTO.getNombreEmpresa()).orElseThrow(
                () -> new ResourceNotFoundException("Empresa", updatedOfertaLaboralDTO.getNombreEmpresa()));

        existingOfertaLaboral.setEmpresa(empresa);

        return mapToDTO(ofertasLaboralesRepository.save(existingOfertaLaboral));
    }

    public OfertasLaboralesDTO actualizarEstadoOferta(Long id, String estado) {
        OfertasLaborales existingOfertaLaboral = ofertasLaboralesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OfertaLaboral", String.valueOf(id)));

        try {
            existingOfertaLaboral.setEstado(EstadoOferta.valueOf(estado));

            createRequestAndSendEmailWithPDF(existingOfertaLaboral);
        } catch (IllegalArgumentException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Estado de oferta laboral no válido: " + estado);
        }

        return mapToDTO(ofertasLaboralesRepository.save(existingOfertaLaboral));
    }

    public OfertasLaboralesDTO findByIdToDTO(Long id) {
        OfertasLaborales ofertaLaboral = ofertasLaboralesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OfertaLaboral", String.valueOf(id)));

        return mapToDTO(ofertaLaboral);
    }

    public void delete(Long id) {
        ofertasLaboralesRepository.deleteById(id);
    }

    public List<OfertasLaboralesDTO> findByNombreUsuario(String nombreUsuario) {
        List<Postulacion> postulacions = postulacionRepository.findAllByGraduadoUsuarioNombreUsuario(nombreUsuario);
        List<OfertasLaborales> referencias = new ArrayList<>();
        for (Postulacion postulacion : postulacions) {
            referencias.add(postulacion.getOfertaLaboral());
        }
        return referencias.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<Map.Entry<String, Long>> calcularPostulacionesPorDia() {

        return postulacionRepository.countPostulacionesPorDia().stream()
                .map(t -> new AbstractMap.SimpleEntry<>((String) t.get(0), (Long) t.get(1)))
                .collect(Collectors.toList());
    }

    public List<OfertasLaboralesDTO> findEmpresarioByNombreUsuario(String nombreUsuario) {
        List<OfertasLaborales> referencias = ofertasLaboralesRepository.buscarOfertasPorNombreUsuario(nombreUsuario);
        return referencias.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Map<String, Long> getCargosConOfertas() {
        List<OfertasLaboralesDTO> ofertasLaboralesDTOList = findAll();

        return ofertasLaboralesDTOList.stream()
                .collect(Collectors.groupingBy(
                        oferta -> StringUtils.hasText(oferta.getCargo()) ?  oferta.getCargo().toLowerCase().trim(): "Sin cargo especificado por estilo",
                        Collectors.counting()
                ));
    }

    public List<Graduado> findGraduadosByOfertaId(Long ofertaId) {
        List<Postulacion> postulantes = postulacionRepository.findAllByOfertaLaboralId(ofertaId);

        return postulantes
                .stream()
                .map(Postulacion::getGraduado)
                .collect(Collectors.toList())
                .stream()
                .peek(g -> {
                    g.getUsuario()
                            .setUrlImagen(s3Service.getObjectUrl(g.getUsuario().getRutaImagen()));
                    g.setUrlPdf(s3Service.getObjectUrl(g.getRutaPdf()));
                }).collect(Collectors.toList());
    }

    public List<Graduado> findGraduadosConPostulacionActivaByOfertaId(Long ofertaId) {
        List<Postulacion> postulantes = postulacionRepository.findAllByOfertaLaboralId(ofertaId);

        return postulantes
                .stream()
                .filter(postulacion -> postulacion.getEstado().equals(EstadoPostulacion.APLICANDO))
                .map(Postulacion::getGraduado)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
                .stream()
                .peek(g -> {
                    if (g != null) {
                        g.getUsuario()
                                .setUrlImagen(s3Service.getObjectUrl(g.getUsuario().getRutaImagen()));
                        g.setUrlPdf(s3Service.getObjectUrl(g.getRutaPdf()));
                    }
                })
                .collect(Collectors.toList());
    }

    public List<Graduado> findGraduadosConPostulacionCanceladaByOfertaId(Long ofertaId) {
        List<Postulacion> postulantes = postulacionRepository.findAllByOfertaLaboralId(ofertaId);

        return postulantes
                .stream()
                .filter(postulacion -> postulacion.getEstado().equals(EstadoPostulacion.CANCELADA_POR_GRADUADO) || postulacion.getEstado().equals(EstadoPostulacion.CANCELADA_POR_ADMINISTRADOR))
                .map(Postulacion::getGraduado)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
                .stream()
                .peek(g -> {
                    if (g != null) {
                        g.getUsuario()
                                .setUrlImagen(s3Service.getObjectUrl(g.getUsuario().getRutaImagen()));
                        g.setUrlPdf(s3Service.getObjectUrl(g.getRutaPdf()));
                    }
                })
                .collect(Collectors.toList());
    }

    public List<Graduado> findGraduadosSeleccionadosByOfertaId(Long ofertaId) {
        List<Postulacion> postulantes = postulacionRepository.findAllByOfertaLaboralId(ofertaId);

        return postulantes
                .stream()
                .filter(postulacion -> postulacion.getEstado().equals(EstadoPostulacion.ACEPTADO))
                .map(Postulacion::getGraduado)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
                .stream()
                .peek(g -> {
                    if (g != null) {
                        g.getUsuario()
                                .setUrlImagen(s3Service.getObjectUrl(g.getUsuario().getRutaImagen()));
                        g.setUrlPdf(s3Service.getObjectUrl(g.getRutaPdf()));
                    }
                })
                .collect(Collectors.toList());
    }

    public List<OfertasLaborales> findOfertasByNombreEmpresa(String nombreEmpresa) {
        return ofertasLaboralesRepository.findOfertasByNombreEmpresa(nombreEmpresa);
    }

    @Deprecated
    @Modifying
    @Transactional
    public Contratacion seleccionarContratados(Long ofertaId, List<Long> graduadosIds) { // Do not use this method
        OfertasLaborales ofertaLaboral = ofertasLaboralesRepository.findById(ofertaId)
                .orElseThrow(() -> new ResourceNotFoundException("OfertaLaboral", String.valueOf(ofertaId)));

        List<Graduado> graduadosSeleccionados = graduadoRepository.findAllById(graduadosIds);

        Contratacion contratacion = new Contratacion();
        contratacion.setOfertaLaboral(ofertaLaboral);
        contratacion.setGraduados(graduadosSeleccionados);

        return contratacionRepository.save(contratacion);
    }

    @Deprecated
    public List<Contratacion> getContratacionesPorOfertaLaboral(Long ofertaLaboralId) { // Do not use this method
        return contratacionRepository.findByOfertaLaboralId(ofertaLaboralId);
    }

    public List<OfertasLaboralesDTO> getOfertasSinPostulacion(Long id) {
        Graduado graduado = graduadoRepository.findByUsuarioId(id).orElseThrow(() -> new ResourceNotFoundException("Graduado no encontrado con el id de usuario: ", String.valueOf(id)));

        return ofertasLaboralesRepository.findOfertasWithOutPostulacionByGraduadoId(graduado.getId()).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private MailRequest createMailRequest(String subject, String mailCase) {
        return new MailRequest(from, subject, mailCase);
    }

    private void createRequestAndSendEmailWithPDF(OfertasLaborales oferta) {
        Map<String, Object> model = new HashMap<>();
        List<Graduado> graduado = graduadoRepository.findAll();
        String[] emails = graduado.stream().map(Graduado::getEmailPersonal).toArray(String[]::new);
        byte[] imageBytes = StringUtils.hasText(oferta.getFotoPortada()) ? imageOptimizer.convertBase64ToBytes(oferta.getFotoPortada()) : new byte[0];

        model.put("oferta", oferta);
        model.put("fotoPortada", imageBytes);

        String subject = getMailSubject(oferta.getEstado().name());
        String mailCase = oferta.getEstado().equals(EstadoOferta.EN_CONVOCATORIA) ? "new-offer" : oferta.getEstado().equals(EstadoOferta.CANCELADA) ? "offer-canceled" : oferta.getEstado().equals(EstadoOferta.FINALIZADA) ? "offer-finished" : oferta.getEstado().equals(EstadoOferta.REACTIVADA) ? "offer-reactivated" : "offer-selection";

        MailRequest request = createMailRequest(subject, mailCase);

        emailService.sendEmailWithPDF(request, model, emails);
    }

    private String getMailSubject(String estado) {
        if (estado.equals(EstadoOferta.EN_CONVOCATORIA.name())) {
            return "¡Nuevo oferta laboral disponible!";
        } else if (estado.equals(EstadoOferta.CANCELADA.name())) {
            return "Oferta laboral cancelada";
        } else if (estado.equals(EstadoOferta.FINALIZADA.name())) {
            return "Oferta laboral finalizada";
        } else if (estado.equals(EstadoOferta.REACTIVADA.name())) {
            return "Oferta laboral reactivada";
        } else {
            return "Oferta laboral en selección";
        }
    }
}