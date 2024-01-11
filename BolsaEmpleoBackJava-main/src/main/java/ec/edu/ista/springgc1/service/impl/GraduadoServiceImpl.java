package ec.edu.ista.springgc1.service.impl;

import ec.edu.ista.springgc1.model.entity.*;
import ec.edu.ista.springgc1.repository.CiudadRepository;
import ec.edu.ista.springgc1.repository.GraduadoRepository;
import ec.edu.ista.springgc1.repository.UsuarioRepository;
import ec.edu.ista.springgc1.service.bucket.S3Service;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.map.Mapper;

import java.util.List;
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
		estudiante.setEmail_personal(estudianteDTO.getEmail_personal());
		estudiante.setEstadocivil(estudianteDTO.getEstadocivil());
		estudiante.setRuta_pdf(estudianteDTO.getRuta_pdf());
		estudiante.setUrl_pdf(
				estudianteDTO.getRuta_pdf() == null ? null : s3Service.getObjectUrl(estudianteDTO.getRuta_pdf()));

		return estudiante;
	}

	@Override
	public GraduadoDTO mapToDTO(Graduado estudiante) {
		GraduadoDTO estudianteDTO = new GraduadoDTO();

		estudianteDTO.setId(estudiante.getId());
		estudianteDTO.setUsuario(estudiante.getUsuario().getNombreUsuario());
		estudianteDTO.setCiudad(estudiante.getCiudad().getNombre());
		estudianteDTO.setAño_graduacion(estudiante.getAño_graduacion());
		estudianteDTO.setEmail_personal(estudiante.getEmail_personal());
		estudianteDTO.setEstadocivil(estudiante.getEstadocivil());
		estudianteDTO.setRuta_pdf(estudiante.getRuta_pdf());
		estudianteDTO.setUrl_pdf(estudiante.getUrl_pdf());

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

		Graduado estudiante = graduadoRepository.findByUsuario(id_usuario)
				.orElseThrow(() -> new ResourceNotFoundException("id_usuario", id_usuario));
		estudiante
				.setUrl_pdf(estudiante.getRuta_pdf() == null ? null : s3Service.getObjectUrl(estudiante.getRuta_pdf()));
		return mapToDTO(estudiante);
	}
	
	public Graduado findByUsuarioPersonaCedulaContaining(String cedula) {
		return graduadoRepository.findByUsuarioPersonaCedulaContaining(cedula)
				.orElseThrow(() -> new ResourceNotFoundException("cedula", cedula));
	}

	@Override
	public Graduado save(Object entity) {

		return graduadoRepository.save(mapToEntity((GraduadoDTO) entity));
	}

	public Long countEstudiantes() {
		return graduadoRepository.count();
	}

}
