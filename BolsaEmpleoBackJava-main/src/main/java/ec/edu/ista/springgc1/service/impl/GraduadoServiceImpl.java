package ec.edu.ista.springgc1.service.impl;

import ec.edu.ista.springgc1.model.entity.*;
import ec.edu.ista.springgc1.repository.CiudadRepository;
import ec.edu.ista.springgc1.repository.GraduadoRepository;
import ec.edu.ista.springgc1.repository.OfertaslaboralesRepository;
import ec.edu.ista.springgc1.repository.UsuarioRepository;
import ec.edu.ista.springgc1.service.bucket.S3Service;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.map.Mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private OfertaslaboralesRepository ofertasRepository;

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
        estudiante.setAño_graduacion(estudianteDTO.getAño_graduacion());
        estudiante.setEmailPersonal(estudianteDTO.getEmail_personal());
        estudiante.setEstadocivil(estudianteDTO.getEstadocivil());
        estudiante.setRuta_pdf(estudianteDTO.getRuta_pdf());
        estudiante.setUrl_pdf(
                estudianteDTO.getRuta_pdf() == null ? null : s3Service.getObjectUrl(estudianteDTO.getRuta_pdf()));

        List<OfertasLaborales> ofertas = new ArrayList<>();
        if (estudianteDTO.getIdOferta() != null) {
            ofertas = ofertasRepository.findOfertasByIdIn(estudianteDTO.getIdOferta());
            estudiante.setOfertas(ofertas);
        }

        return estudiante;
    }


    @Override
    public GraduadoDTO mapToDTO(Graduado estudiante) {
        GraduadoDTO estudianteDTO = new GraduadoDTO();

        estudianteDTO.setId(estudiante.getId());
        estudianteDTO.setUsuario(estudiante.getUsuario().getNombreUsuario());
        estudianteDTO.setCiudad(estudiante.getCiudad().getNombre());
        estudianteDTO.setAño_graduacion(estudiante.getAño_graduacion());
        estudianteDTO.setEmail_personal(estudiante.getEmailPersonal());
        estudianteDTO.setEstadocivil(estudiante.getEstadocivil());
        estudianteDTO.setRuta_pdf(estudiante.getRuta_pdf());
        estudianteDTO.setUrl_pdf(estudiante.getUrl_pdf());
        List<Long> idOfertas = new ArrayList<>();
        if (estudiante.getOfertas() != null) {
            for (OfertasLaborales oferta : estudiante.getOfertas()) {
                if (oferta.getId() != null) {
                    idOfertas.add(oferta.getId());
                }
            }
        }
        estudianteDTO.setIdOferta(idOfertas);

        return estudianteDTO;
    }

    @Override
    public List findAll() {
        return graduadoRepository.findAll().stream()
                .peek(e -> e.setUrl_pdf(e.getRuta_pdf() == null ? null : s3Service.getObjectUrl(e.getRuta_pdf())))
                .map(e -> mapToDTO(e)).collect(Collectors.toList());
    }

    public GraduadoDTO findByIdToDTO(Long id) {
        Graduado estudiante = graduadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id", id));

        return mapToDTO(estudiante);
    }

    public GraduadoDTO findByUsuario(long id_usuario) {

        Graduado estudiante = graduadoRepository.findByUsuarioId(id_usuario)
                .orElseThrow(() -> new ResourceNotFoundException("id_usuario", id_usuario));
        estudiante
                .setUrl_pdf(estudiante.getRuta_pdf() == null ? null : s3Service.getObjectUrl(estudiante.getRuta_pdf()));
        return mapToDTO(estudiante);
    }

    public Graduado findByUsuarioPersonaCedulaContaining(String cedula) {
        return graduadoRepository.findByUsuarioPersonaCedulaContaining(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("cedula", cedula));
    }

    public List<OfertasLaborales> findByUsuarioNombreUsuario(String username) {
        Graduado graduado = graduadoRepository.findByUsuarioNombreUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("usuario", username));

        List<OfertasLaborales> ofertas = graduado.getOfertas();

        return ofertas;
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

        graduadoFromDb.setAño_graduacion(estudianteDTO.getAño_graduacion());
        graduadoFromDb.setCiudad(ciudadRepository.findByNombre(estudianteDTO.getCiudad())
                .orElseThrow(() -> new ResourceNotFoundException("Ciudad", estudianteDTO.getCiudad())));

        graduadoFromDb.setEmailPersonal(estudianteDTO.getEmail_personal());
        graduadoFromDb.setEstadocivil(estudianteDTO.getEstadocivil());
        graduadoFromDb.setRuta_pdf(estudianteDTO.getRuta_pdf());
        graduadoFromDb.setUrl_pdf(estudianteDTO.getUrl_pdf());

        List<OfertasLaborales> existingOfertas = graduadoFromDb.getOfertas();

        List<OfertasLaborales> nuevasOfertas = ofertasRepository.findOfertasByIdIn(estudianteDTO.getIdOferta());

        graduadoFromDb.setOfertas(nuevasOfertas);

        for (OfertasLaborales oferta : existingOfertas) {
            oferta.getGraduados().remove(graduadoFromDb);
        }

        for (OfertasLaborales nuevaOferta : nuevasOfertas) {
            nuevaOferta.getGraduados().add(graduadoFromDb);
        }

        return mapToDTO(graduadoRepository.save(graduadoFromDb));
    }

    public GraduadoDTO updatePostulacion(Long idGraduado, Long ofertas) {
        Graduado estudiante = graduadoRepository.findById(idGraduado)
                .orElseThrow(() -> new ResourceNotFoundException("id", idGraduado));

        GraduadoDTO graduadoFromDb = mapToDTO(estudiante);

        graduadoFromDb.getIdOferta().add(ofertas);

        return graduadoFromDb;
    }

    public GraduadoDTO cancelPostulacion(Long idGraduado, Long ofertas) {
        Graduado estudiante = graduadoRepository.findById(idGraduado)
                .orElseThrow(() -> new ResourceNotFoundException("id", idGraduado));

        GraduadoDTO graduadoFromDb = mapToDTO(estudiante);

        graduadoFromDb.getIdOferta().remove(ofertas);

        return graduadoFromDb;
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
                .peek(e -> e.setUrl_pdf(e.getRuta_pdf() == null ? null : s3Service.getObjectUrl(e.getRuta_pdf())))
                .collect(Collectors.toList());
    }

    public Map<String, Object> countBySex() {
        Map<String, Object> countSex = new HashMap<>();

        countSex.put("masculino", graduadoRepository.countAllByUsuarioPersonaSexo(Persona.Sex.MASCULINO));
        countSex.put("femenino", graduadoRepository.countAllByUsuarioPersonaSexo(Persona.Sex.FEMENINO));
        countSex.put("otro", graduadoRepository.countAllByUsuarioPersonaSexo(Persona.Sex.OTRO));

        return countSex;
    }
}
