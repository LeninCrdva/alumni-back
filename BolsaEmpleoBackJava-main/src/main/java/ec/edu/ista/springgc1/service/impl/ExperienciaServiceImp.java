package ec.edu.ista.springgc1.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.edu.ista.springgc1.exception.ResourceNotFoundException;
import ec.edu.ista.springgc1.model.dto.ExperienciaDTO;
import ec.edu.ista.springgc1.model.entity.Experiencia;
import ec.edu.ista.springgc1.model.entity.Graduado;
import ec.edu.ista.springgc1.repository.ExperienciaRepository;
import ec.edu.ista.springgc1.repository.GraduadoRepository;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.map.Mapper;

@Service
public class ExperienciaServiceImp extends GenericServiceImpl<Experiencia>
		implements Mapper<Experiencia, ExperienciaDTO> {

	@Autowired
	private ExperienciaRepository experienciaRepository;

	@Autowired
	private GraduadoRepository graduadoRepository;

	@Override
	public Experiencia mapToEntity(ExperienciaDTO experienciaDTO) {
		Experiencia experiencia = new Experiencia();
		Graduado graduado = graduadoRepository.findByUsuarioPersonaCedulaContaining(experienciaDTO.getCedulaGraduado())
				.orElseThrow(() -> new ResourceNotFoundException("De Graduado", experienciaDTO.getInstitucionNombre()));

		experiencia.setId(experienciaDTO.getId());
		experiencia.setGraduado(graduado);
		experiencia.setInstitucion(experienciaDTO.getInstitucionNombre());
		experiencia.setActividad(experienciaDTO.getActividad());
		experiencia.setCargo(experienciaDTO.getCargo());
		experiencia.setDuracion(experienciaDTO.getDuracion());
        experiencia.setAreatrabajo(experienciaDTO.getArea_trabajo());
		return experiencia;
	}

	@Override
	public ExperienciaDTO mapToDTO(Experiencia experiencia) {
		ExperienciaDTO experienciaDTO = new ExperienciaDTO();
		experienciaDTO.setId(experiencia.getId());
		experienciaDTO.setCedulaGraduado(experiencia.getGraduado().getUsuario().getPersona().getCedula());
		experienciaDTO.setInstitucionNombre(experiencia.getInstitucion());
		experienciaDTO.setActividad(experiencia.getActividad());
		experienciaDTO.setCargo(experiencia.getCargo());
		experienciaDTO.setDuracion(experiencia.getDuracion());
		experienciaDTO.setArea_trabajo(experiencia.getAreatrabajo());
		return experienciaDTO;
	}

	@Override
	public List<?> findAll() {
		return experienciaRepository.findAll().stream().map(c -> mapToDTO(c)).collect(Collectors.toList());
	}

	public ExperienciaDTO findByIdToDTO(Long id) {
		Experiencia experiencia = experienciaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("id", id));

		return mapToDTO(experiencia);
	}

	public Graduado findByUsuarioPersonaCedulaContaining(String cedula) {
		return graduadoRepository.findByUsuarioPersonaCedulaContaining(cedula)
				.orElseThrow(() -> new ResourceNotFoundException("cedula", cedula));
	}

	@Override
	public Experiencia save(Object entity) {
		return experienciaRepository.save(mapToEntity((ExperienciaDTO) entity));
	}
}
