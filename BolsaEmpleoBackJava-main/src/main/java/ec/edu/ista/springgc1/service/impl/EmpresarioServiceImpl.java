package ec.edu.ista.springgc1.service.impl;


import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.edu.ista.springgc1.exception.ResourceNotFoundException;
import ec.edu.ista.springgc1.model.dto.EmpresarioDTO;
import ec.edu.ista.springgc1.model.dto.SuperAdminDTO;
import ec.edu.ista.springgc1.model.entity.Empresario;
import ec.edu.ista.springgc1.model.entity.Usuario;
import ec.edu.ista.springgc1.repository.EmpresarioRepository;
import ec.edu.ista.springgc1.repository.UsuarioRepository;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.map.Mapper;
@Service
public class EmpresarioServiceImpl extends GenericServiceImpl<Empresario> implements Mapper<Empresario, EmpresarioDTO>{
	 @Autowired
		private EmpresarioRepository empresariorepository;
		  @Autowired
		    private UsuarioRepository usuarioRepository;

		   

		    @Override
		    public Empresario mapToEntity(EmpresarioDTO empresarioDTO) {
		    	Empresario empresario = new Empresario();

		        Usuario usuario = usuarioRepository.findBynombreUsuario(empresarioDTO.getUsuario())
		                .orElseThrow(() -> new ResourceNotFoundException("nombre de usuario", empresarioDTO.getUsuario()));

		        empresario.setId(empresarioDTO.getId());
		        empresario.setUsuario(usuario);
		        empresario.setEstado(empresarioDTO.isEstado());
		        empresario.setAnios(empresarioDTO.getAnios());
		        empresario.setPuesto(empresarioDTO.getPuesto());
		       
		       
		       

		        return empresario;
		    }

		    @Override
		    public EmpresarioDTO mapToDTO(Empresario empresario) {
		    	EmpresarioDTO empresarioDTO = new EmpresarioDTO();
		    	empresarioDTO.setId(empresario.getId());
		    	empresarioDTO.setUsuario(empresario.getUsuario().getNombreUsuario());
		    	empresarioDTO.setPuesto(empresario.getPuesto());
		    	empresarioDTO.setEstado(empresario.isEstado());
		    	empresarioDTO.setAnios(empresario.getAnios());
		        return empresarioDTO;
		    }
		    @Override
		    public List findAll() {
		        return empresariorepository.findAll()
		                .stream()
		                .map(c -> mapToDTO(c))
		                .collect(Collectors.toList());
		    }
		    public EmpresarioDTO findByIdToDTO(Long id) {
		    	Empresario empresario = empresariorepository.findById(id)
		                .orElseThrow(() -> new ResourceNotFoundException("id", id));

		        return mapToDTO(empresario);
		    }


		   



		    public EmpresarioDTO findByUsuario(long id_usuario) {

		    	Empresario empresario = empresariorepository.findByUsuario(id_usuario)
		    			   .orElseThrow(() -> new ResourceNotFoundException("id_usuario", id_usuario));

		                
		        return mapToDTO(empresario);
		    }

		  

		    @Override
		    public Empresario save(Object entity) {

		        return empresariorepository.save(mapToEntity((EmpresarioDTO) entity));
		    }
		    public void delete(Long id) {
		    	empresariorepository.deleteById(id);
		    }
		    public EmpresarioDTO update(Long id, EmpresarioDTO updatedEmpresarioDTO) {
		        Empresario existingEmpresario = empresariorepository.findById(id)
		                .orElseThrow(() -> new ResourceNotFoundException("Empresario", String.valueOf(id)));

		        
		        String nuevoUsuario = updatedEmpresarioDTO.getUsuario();
		      

		       
		        existingEmpresario.setUsuario(usuarioRepository.findBynombreUsuario(nuevoUsuario)
		                .orElseThrow(() -> new ResourceNotFoundException("nombre de usuario", nuevoUsuario)));
		        existingEmpresario.setEstado(updatedEmpresarioDTO.isEstado());
		        existingEmpresario.setAnios(updatedEmpresarioDTO.getAnios());
		        existingEmpresario.setPuesto(updatedEmpresarioDTO.getPuesto());

		        return mapToDTO(empresariorepository.save(existingEmpresario));
		    }
		  


}
