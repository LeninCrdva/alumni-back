package ec.edu.ista.springgc1.service.impl;

import ec.edu.ista.springgc1.exception.AppException;
import ec.edu.ista.springgc1.exception.ResourceNotFoundException;
import ec.edu.ista.springgc1.model.dto.UsuarioDTO;
import ec.edu.ista.springgc1.model.entity.Rol;
import ec.edu.ista.springgc1.model.entity.Usuario;
import ec.edu.ista.springgc1.repository.RolRepository;
import ec.edu.ista.springgc1.repository.UsuarioRepository;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.map.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl extends GenericServiceImpl<Usuario> implements Mapper<Usuario, UsuarioDTO> {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario mapToEntity(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        Rol rol = rolRepository.findByNombre(usuarioDTO.getRol())
                .orElseThrow(() -> new ResourceNotFoundException("nombre", usuarioDTO.getRol()));
        usuario.setId(usuarioDTO.getId());
        usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
        usuario.setClave(passwordEncoder.encode(usuarioDTO.getClave()));
       /*usuario.setAdmin(usuarioDTO.getAdmin());
        usuario.setEstado(usuarioDTO.isEstado());
        usuario.setEmpresario(usuarioDTO.getEmpresario());
        usuario.setGraduado(usuarioDTO.getGraduado());
        usuario.setSuperadmin(usuarioDTO.getSuperadmin());*/
        usuario.setUsuarioTipo(usuarioDTO.getUsuarioTipo());
       
        usuario.setRol(rol);

        return usuario;
    }

    @Override
    public UsuarioDTO mapToDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setClave(usuario.getClave());
        usuarioDTO.setNombreUsuario(usuario.getNombreUsuario());
       /* usuarioDTO.setGraduado(usuario.getGraduado());
        usuarioDTO.setEstado(usuario.getEstado());
        usuarioDTO.setAdmin(usuario.getAdmin());
        usuarioDTO.setEmpresario(usuario.getEmpresario());
        usuarioDTO.setSuperadmin(usuario.getSuperadmin());*/
        usuarioDTO.setUsuarioTipo(usuario.getUsuarioTipo());
        usuarioDTO.setRol(usuario.getRol().getNombre());
        return usuarioDTO;
    }

    @Override
    public List findAll() {
        return usuarioRepository.findAll().stream()
                .map(u -> mapToDTO(u))
                .collect(Collectors.toList());
    }

    public UsuarioDTO findByIdToDTO(long id) {
        return mapToDTO(usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id", id)));
    }

    public Usuario findByUsername(String username) {
        return usuarioRepository.findBynombreUsuario(username).orElseThrow(() -> new ResourceNotFoundException("username", username));
    }

    public Usuario update(long id, UsuarioDTO usuarioDTO) {

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id", id));
        if (!usuario.getNombreUsuario().equalsIgnoreCase(usuarioDTO.getNombreUsuario()) && usuarioRepository.existsBynombreUsuario(usuarioDTO.getNombreUsuario())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Username ya se encuentra en otro registro");
        }
        Rol rol = rolRepository.findByNombre(usuarioDTO.getRol())
                .orElseThrow(() -> new ResourceNotFoundException("nombre", usuarioDTO.getRol()));

        usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
        if (!usuarioDTO.getClave().isEmpty()) {
            usuario.setClave(passwordEncoder.encode(usuarioDTO.getClave()));
        }
        /*usuarioDTO.setAdmin(usuario.getAdmin());
        usuarioDTO.setEmpresario(usuario.getEmpresario());
        usuarioDTO.setSuperadmin(usuario.getSuperadmin());*/
        usuarioDTO.setUsuarioTipo(usuario.getUsuarioTipo());
        
        /*usuario.setGraduado(usuarioDTO.getGraduado());
        usuario.setAdmin(usuarioDTO.getAdmin());
        usuario.setEmpresario(usuarioDTO.getEmpresario());
        usuario.setSuperadmin(usuarioDTO.getSuperadmin());*/
        usuario.setUsuarioTipo(usuarioDTO.getUsuarioTipo());
        usuario.setEstado(usuarioDTO.isEstado());
        usuario.setRol(rol);

        return usuarioRepository.save(usuario);

    }

    @Override
    public Usuario save(Object entity) {
        return usuarioRepository.save(mapToEntity((UsuarioDTO) entity));
    }

    @Transactional
    public Boolean existsByUsername(String username) {
        return usuarioRepository.existsBynombreUsuario(username);
    }
}
