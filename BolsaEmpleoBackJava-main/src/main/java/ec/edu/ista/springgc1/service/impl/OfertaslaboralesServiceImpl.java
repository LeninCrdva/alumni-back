package ec.edu.ista.springgc1.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.edu.ista.springgc1.exception.ResourceNotFoundException;
import ec.edu.ista.springgc1.model.dto.OfertasLaboralesDTO;
import ec.edu.ista.springgc1.model.entity.Empresa;
import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.model.entity.OfertasLaborales;
import ec.edu.ista.springgc1.repository.EmpresaRepository;
import ec.edu.ista.springgc1.repository.GraduadoRepository;
import ec.edu.ista.springgc1.repository.OfertaslaboralesRepository;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.map.Mapper;

@Service
public class OfertaslaboralesServiceImpl extends GenericServiceImpl<OfertasLaborales>
		implements Mapper<OfertasLaborales, OfertasLaboralesDTO> {

	@Autowired
	private OfertaslaboralesRepository ofertasLaboralesRepository;

	@Autowired
	private EmpresaRepository empresarepository;
	
	@Autowired
	private GraduadoRepository graduadoRepository;
	
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
		
		List<Graduado> graduados = new ArrayList<>();
		if(dto.getCorreoGraduado() == null && !dto.getCorreoGraduado().isEmpty()) {
			graduados = graduadoRepository.findByEmailPersonalIn(dto.getCorreoGraduado());
			ofertaLaboral.setGraduados(graduados);
		}
		
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
		Set<String> correoGraduado = new HashSet<>();
		if(entity.getGraduados() != null) {
			for (Graduado graduado : entity.getGraduados()) {
				if(graduado.getEmailPersonal() != null) {
					correoGraduado.add(graduado.getEmailPersonal());
				}
			}
		}
		
		dto.setCorreoGraduado(correoGraduado);

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

	    Empresa empresa = empresarepository.findByNombre(updatedOfertaLaboralDTO.getNombreEmpresa())
	            .orElseThrow(() -> new ResourceNotFoundException("Empresa", updatedOfertaLaboralDTO.getNombreEmpresa()));

	    List<Graduado> existingGraduados = existingOfertaLaboral.getGraduados();

	    List<Graduado> nuevosGraduados = graduadoRepository.findByEmailPersonalIn(updatedOfertaLaboralDTO.getCorreoGraduado());

	    existingOfertaLaboral.setGraduados(nuevosGraduados);

	    for (Graduado graduado : existingGraduados) {
	        graduado.getOfertas().remove(existingOfertaLaboral);
	    }

	    for (Graduado nuevoGraduado : nuevosGraduados) {
	        nuevoGraduado.getOfertas().add(existingOfertaLaboral);
	    }

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
}
