package ec.edu.ista.springgc1.service.impl;

import ec.edu.ista.springgc1.model.entity.*;
import ec.edu.ista.springgc1.repository.*;
import ec.edu.ista.springgc1.service.bucket.S3Service;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.map.Mapper;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.edu.ista.springgc1.exception.ResourceNotFoundException;
import ec.edu.ista.springgc1.model.dto.*;

@Service
public class GraduadoServiceImpl extends GenericServiceImpl<Graduado> implements Mapper<Graduado, GraduadoDTO> {

    @Autowired
    private GraduadoRepository graduadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CiudadRepository ciudadRepository;

    @Autowired
    private PostulacionRepository postulacionRepository;

    @Autowired
    private S3Service s3Service;

    @Override
    public Graduado mapToEntity(GraduadoDTO estudianteDTO) {
        Graduado estudiante = new Graduado();

        Usuario usuario = usuarioRepository.findBynombreUsuario(estudianteDTO.getUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("usuario", estudianteDTO.getUsuario()));

        Ciudad ciudad = ciudadRepository.findByNombre(estudianteDTO.getCiudad())
                .orElseThrow(() -> new ResourceNotFoundException("ciudad", estudianteDTO.getCiudad()));

        estudiante.setId(estudianteDTO.getId());
        estudiante.setUsuario(usuario);
        estudiante.setCiudad(ciudad);
        estudiante.setAnioGraduacion(estudianteDTO.getAnioGraduacion());
        estudiante.setEmailPersonal(estudianteDTO.getEmailPersonal());
        estudiante.setEstadoCivil(estudianteDTO.getEstadoCivil());
        estudiante.setRutaPdf(estudianteDTO.getRutaPdf());
        estudiante.setUrlPdf(
                estudianteDTO.getRutaPdf() == null ? null : s3Service.getObjectUrl(estudianteDTO.getRutaPdf()));

        return estudiante;
    }

    @Override
    public GraduadoDTO mapToDTO(Graduado estudiante) {
        GraduadoDTO estudianteDTO = new GraduadoDTO();

        estudianteDTO.setId(estudiante.getId());
        estudianteDTO.setUsuario(estudiante.getUsuario().getNombreUsuario());
        estudianteDTO.setCiudad(estudiante.getCiudad().getNombre());
        estudianteDTO.setAnioGraduacion(estudiante.getAnioGraduacion());
        estudianteDTO.setEmailPersonal(estudiante.getEmailPersonal());
        estudianteDTO.setEstadoCivil(estudiante.getEstadoCivil());
        estudianteDTO.setRutaPdf(estudiante.getRutaPdf());
        estudianteDTO.setUrlPdf(s3Service.getObjectUrl(estudiante.getRutaPdf()));

        return estudianteDTO;
    }

    @Override
    public List findAll() {
        return graduadoRepository.findAll().stream()
                .peek(e -> e.setUrlPdf(e.getRutaPdf() == null ? null : s3Service.getObjectUrl(e.getRutaPdf())))
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    public GraduadoDTO findByIdToDTO(Long id) {
        Graduado estudiante = graduadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id", id));

        return mapToDTO(estudiante);
    }

    public GraduadoDTO findByUsuario(long id_usuario) {

        Graduado estudiante = graduadoRepository.findByUsuarioId(id_usuario)
                .map(e -> {
                    e.setUrlPdf(e.getRutaPdf() == null ? null : s3Service.getObjectUrl(e.getRutaPdf()));
                    return e;
                }).orElseThrow(() -> new ResourceNotFoundException("id_usuario", id_usuario));

        return mapToDTO(estudiante);
    }

    public List<Graduado> findAllGraduadosNotIn(Long id){
        return graduadoRepository.findByUsuarioIdNot(id);
    }

    public Graduado findByIdUsuario(long id_usuario) {
        return graduadoRepository.findByUsuarioId(id_usuario)
                .map(e -> {
                    e.setUrlPdf(e.getRutaPdf() == null ? null : s3Service.getObjectUrl(e.getRutaPdf()));
                    return e;
                })
                .orElseThrow(() -> new ResourceNotFoundException("id_usuario", id_usuario));
    }

    public Graduado findByUsuarioPersonaCedulaContaining(String cedula) {
        return graduadoRepository.findByUsuarioPersonaCedulaContaining(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("cedula", cedula));
    }

    public List<OfertasLaborales> findByUsuarioNombreUsuario(String username) {
        List<Postulacion> postulacion = postulacionRepository.findAllByGraduadoUsuarioNombreUsuario(username);

        return postulacion.stream()
                .map(Postulacion::getOfertaLaboral)
                .collect(Collectors.toList());
    }

    @Override
    public Graduado save(Object entity) {
        return graduadoRepository.save(mapToEntity((GraduadoDTO) entity));
    }

    public GraduadoDTO update(Long id, GraduadoDTO estudianteDTO) {
        Graduado graduadoFromDb = graduadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Graduado", id));

        graduadoFromDb.setUsuario(usuarioRepository.findBynombreUsuario(estudianteDTO.getUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", estudianteDTO.getUsuario())));

        graduadoFromDb.setAnioGraduacion(estudianteDTO.getAnioGraduacion());
        graduadoFromDb.setCiudad(ciudadRepository.findByNombre(estudianteDTO.getCiudad())
                .orElseThrow(() -> new ResourceNotFoundException("Ciudad", estudianteDTO.getCiudad())));

        graduadoFromDb.setEmailPersonal(estudianteDTO.getEmailPersonal());
        graduadoFromDb.setEstadoCivil(estudianteDTO.getEstadoCivil());
        graduadoFromDb.setRutaPdf(estudianteDTO.getRutaPdf());
        graduadoFromDb.setUrlPdf(estudianteDTO.getUrlPdf());

        return mapToDTO(graduadoRepository.save(graduadoFromDb));
    }

    public Graduado findByEmail(String email) {
        return graduadoRepository.findByEmailPersonal(email)
                .orElse(new Graduado());
    }

    public Long countEstudiantes() {
        return graduadoRepository.count();
    }

    public List<Graduado> findGRaduadoWithOutOfertas() {
        return graduadoRepository.findAllGraduadosWithoutOfertas();
    }

    public List<Graduado> findAllGraduados() {
        return graduadoRepository.findAll().stream()
                .peek(e -> e.setUrlPdf(e.getRutaPdf() == null ? null : s3Service.getObjectUrl(e.getRutaPdf())))
                .collect(Collectors.toList());
    }

    public Map<String, Object> countBySex() {
        Map<String, Object> countSex = new LinkedHashMap<>();

        countSex.put("masculino", graduadoRepository.countAllByUsuarioPersonaSexo(Persona.Sex.MASCULINO));
        countSex.put("femenino", graduadoRepository.countAllByUsuarioPersonaSexo(Persona.Sex.FEMENINO));
        countSex.put("otro", graduadoRepository.countAllByUsuarioPersonaSexo(Persona.Sex.OTRO));

        return countSex;
    }

    public List<Graduado> findGraduadosWithOfertas() {
        return graduadoRepository.findAllGraduadosWithOfertas();
    }

    public List<Graduado> findGraduadosSinExperiencia() {
        return graduadoRepository.findAllGraduadosSinExperiencia();
    }
}
