package ec.edu.ista.springgc1.service.impl;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import ec.edu.ista.springgc1.model.dto.GraduadoDTO;
import ec.edu.ista.springgc1.model.entity.*;
import ec.edu.ista.springgc1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ec.edu.ista.springgc1.exception.ResourceNotFoundException;
import ec.edu.ista.springgc1.model.dto.OfertasLaboralesDTO;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.map.Mapper;

@Service
public class OfertaslaboralesServiceImpl extends GenericServiceImpl<OfertasLaborales>
        implements Mapper<OfertasLaborales, OfertasLaboralesDTO> {

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

    @Override
    public OfertasLaborales mapToEntity(OfertasLaboralesDTO dto) {
        OfertasLaborales ofertaLaboral = new OfertasLaborales();
        ofertaLaboral.setId(dto.getId());
        ofertaLaboral.setSalario(dto.getSalario());
        ofertaLaboral.setFecha_cierre(dto.getFechaCierre());
        ofertaLaboral.setFecha_apertura(dto.getFechaApertura());
        ofertaLaboral.setCargo(dto.getCargo());
        ofertaLaboral.setExperiencia(dto.getExperiencia());
        ofertaLaboral.setFechaPublicacion(dto.getFechaPublicacion());
        ofertaLaboral.setArea_conocimiento(dto.getAreaConocimiento());
        ofertaLaboral.setEstado(dto.getEstado());
        Empresa emp = empresarepository.findByNombre(dto.getNombreEmpresa())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", dto.getNombreEmpresa()));

        ofertaLaboral.setEmpresa(emp);

        return ofertaLaboral;
    }

    @Override
    public OfertasLaboralesDTO mapToDTO(OfertasLaborales entity) {
        OfertasLaboralesDTO dto = new OfertasLaboralesDTO();
        dto.setId(entity.getId());
        dto.setSalario(entity.getSalario());
        dto.setFechaCierre(entity.getFecha_cierre());
        dto.setFechaPublicacion(entity.getFechaPublicacion());
        dto.setCargo(entity.getCargo());
        dto.setExperiencia(entity.getExperiencia());
        dto.setFechaApertura(entity.getFecha_apertura());
        dto.setAreaConocimiento(entity.getArea_conocimiento());
        dto.setEstado(entity.getEstado());
        dto.setNombreEmpresa(entity.getEmpresa().getNombre());

        return dto;
    }

    @Override
    public List<OfertasLaboralesDTO> findAll() {
        return ofertasLaboralesRepository.findAll().stream().map(c -> mapToDTO(c)).collect(Collectors.toList());
    }

    @Override
    public OfertasLaborales save(Object entity) {
        return ofertasLaboralesRepository.save(mapToEntity((OfertasLaboralesDTO) entity));
    }

    public OfertasLaboralesDTO update(Long id, OfertasLaboralesDTO updatedOfertaLaboralDTO) {
        OfertasLaborales existingOfertaLaboral = ofertasLaboralesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OfertaLaboral", String.valueOf(id)));

        existingOfertaLaboral.setSalario(updatedOfertaLaboralDTO.getSalario());
        existingOfertaLaboral.setFecha_cierre(updatedOfertaLaboralDTO.getFechaCierre());
        existingOfertaLaboral.setFecha_apertura(updatedOfertaLaboralDTO.getFechaApertura());
        existingOfertaLaboral.setCargo(updatedOfertaLaboralDTO.getCargo());
        existingOfertaLaboral.setExperiencia(updatedOfertaLaboralDTO.getExperiencia());
        existingOfertaLaboral.setFechaPublicacion(updatedOfertaLaboralDTO.getFechaPublicacion());
        existingOfertaLaboral.setArea_conocimiento(updatedOfertaLaboralDTO.getAreaConocimiento());
        existingOfertaLaboral.setEstado(updatedOfertaLaboralDTO.getEstado());

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
            LocalDate fechaPublicacion = oferta.getFechaPublicacion();

            if (fechaPublicacion != null) {
                postulacionesPorDia.merge(fechaPublicacion, 1L, Long::sum);
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

        return postulaciones.stream().map(Postulacion::getGraduado).collect(Collectors.toList());
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
}
