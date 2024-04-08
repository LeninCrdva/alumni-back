package ec.edu.ista.springgc1.service.impl;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import ec.edu.ista.springgc1.model.dto.MailRequest;
import ec.edu.ista.springgc1.model.entity.*;
import ec.edu.ista.springgc1.repository.*;
import ec.edu.ista.springgc1.service.bucket.S3Service;
import ec.edu.ista.springgc1.service.mail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import ec.edu.ista.springgc1.exception.ResourceNotFoundException;
import ec.edu.ista.springgc1.model.dto.OfertasLaboralesDTO;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.map.Mapper;

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

    @Override
    public OfertasLaborales save(Object entity) {
        OfertasLaborales ofertaLaboral = ofertasLaboralesRepository.save(mapToEntity((OfertasLaboralesDTO) entity));

        createRequestAndSendEmailWithOutPDF(ofertaLaboral);

        return ofertaLaboral;
    }

    public OfertasLaboralesDTO update(Long id, OfertasLaboralesDTO updatedOfertaLaboralDTO) {
        OfertasLaborales existingOfertaLaboral = ofertasLaboralesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OfertaLaboral", String.valueOf(id)));

        existingOfertaLaboral.setSalario(updatedOfertaLaboralDTO.getSalario());
        existingOfertaLaboral.setFechaCierre(updatedOfertaLaboralDTO.getFechaCierre());
        existingOfertaLaboral.setFechaApertura(updatedOfertaLaboralDTO.getFechaApertura());
        existingOfertaLaboral.setCargo(updatedOfertaLaboralDTO.getCargo());
        existingOfertaLaboral.setExperiencia(updatedOfertaLaboralDTO.getExperiencia());
        existingOfertaLaboral.setFechaPublicacion(updatedOfertaLaboralDTO.getFechaPublicacion());
        existingOfertaLaboral.setAreaConocimiento(updatedOfertaLaboralDTO.getAreaConocimiento());
        existingOfertaLaboral.setEstado(updatedOfertaLaboralDTO.getEstado());
        existingOfertaLaboral.setFotoPortada(updatedOfertaLaboralDTO.getFotoPortada());
        existingOfertaLaboral.setTipo(updatedOfertaLaboralDTO.getTipo());
        existingOfertaLaboral.setTiempo(updatedOfertaLaboralDTO.getTiempo());
        Empresa empresa = empresarepository.findByNombre(updatedOfertaLaboralDTO.getNombreEmpresa()).orElseThrow(
                () -> new ResourceNotFoundException("Empresa", updatedOfertaLaboralDTO.getNombreEmpresa()));

        existingOfertaLaboral.setEmpresa(empresa);

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

    public Map<LocalDate, Long> calcularPostulacionesPorDia() {
        List<OfertasLaborales> ofertasLaborales = ofertasLaboralesRepository.findAll();

        Map<LocalDate, Long> postulacionesPorDia = new HashMap<>();

        for (OfertasLaborales oferta : ofertasLaborales) {
            LocalDateTime fechaPublicacion = oferta.getFechaPublicacion();

            if (fechaPublicacion != null) {
                postulacionesPorDia.merge(LocalDate.from(fechaPublicacion), 1L, Long::sum);
            }
        }

        return postulacionesPorDia;
    }

    public List<OfertasLaboralesDTO> findEmpresarioByNombreUsuario(String nombreUsuario) {
        List<OfertasLaborales> referencias = ofertasLaboralesRepository.buscarOfertasPorNombreUsuario(nombreUsuario);
        return referencias.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Map<String, Long> getCargosConOfertas() {
        List<OfertasLaboralesDTO> ofertasLaboralesDTOList = findAll();

        return ofertasLaboralesDTOList.stream()
                .collect(Collectors.groupingBy(
                        oferta -> oferta.getCargo().toLowerCase().trim(),
                        Collectors.counting()
                ));
    }

    public List<Graduado> findGraduadosByOfertaId(Long ofertaId) {
        List<Postulacion> postulaciones = postulacionRepository.findAllByOfertaLaboralId(ofertaId);

        return postulaciones.stream().map(Postulacion::getGraduado).collect(Collectors.toList()).stream().peek(g -> g.getUsuario().setUrlImagen(s3Service.getObjectUrl(g.getUsuario().getRutaImagen()))).collect(Collectors.toList());
    }

    public List<OfertasLaborales> findOfertasByNombreEmpresa(String nombreEmpresa) {
        return ofertasLaboralesRepository.findOfertasByNombreEmpresa(nombreEmpresa);
    }

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

    private void createRequestAndSendEmailWithOutPDF(OfertasLaborales offer) {
        Map<String, Object> model = new HashMap<>();
        List<Graduado> graduado = graduadoRepository.findAll();
        String[] emails = graduado.stream().map(Graduado::getEmailPersonal).toArray(String[]::new);

        model.put("oferta", offer);

        String subject = "¡Nuevo oferta laboral disponible!";
        String mailCase = "new-offer";

        MailRequest request = createMailRequest(subject, mailCase);

        emailService.sendEmail(request, model, emails);
    }
}