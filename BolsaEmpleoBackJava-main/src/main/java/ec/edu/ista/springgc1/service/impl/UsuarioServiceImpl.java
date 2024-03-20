package ec.edu.ista.springgc1.service.impl;

import ec.edu.ista.springgc1.exception.AppException;
import ec.edu.ista.springgc1.exception.ResourceNotFoundException;
import ec.edu.ista.springgc1.model.dto.RegistroDTO;
import ec.edu.ista.springgc1.model.dto.UsuarioDTO;
import ec.edu.ista.springgc1.model.entity.Persona;
import ec.edu.ista.springgc1.model.entity.Rol;
import ec.edu.ista.springgc1.model.entity.Usuario;
import ec.edu.ista.springgc1.repository.PersonaRepository;
import ec.edu.ista.springgc1.repository.RolRepository;
import ec.edu.ista.springgc1.repository.UsuarioRepository;
import ec.edu.ista.springgc1.service.bucket.S3Service;
import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.map.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.kms.model.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl extends GenericServiceImpl<Usuario> implements Mapper<Usuario, UsuarioDTO> {
    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private S3Service s3Service;

    @Override
    public Usuario mapToEntity(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        Rol rol = rolRepository.findByNombre(usuarioDTO.getRol())
                .orElseThrow(() -> new ResourceNotFoundException("nombre", usuarioDTO.getRol()));
        usuario.setId(usuarioDTO.getId());
        usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
        usuario.setClave(passwordEncoder.encode(usuarioDTO.getClave()));
      
        Persona p = personaRepository.findBycedula(usuarioDTO.getCedula())
                .orElseThrow(() -> new ResourceNotFoundException("cedula", usuarioDTO.getCedula()));

        usuario.setRuta_imagen(usuarioDTO.getRuta_imagen());
        usuario.setEstado(usuarioDTO.isEstado());
        usuario.setUrl_imagen(usuarioDTO.getUrl_imagen() == null ? null : s3Service.getObjectUrl(usuarioDTO.getRuta_imagen()));
        usuario.setPersona(p);
        usuario.setRol(rol);

        return usuario;
    }

    @Override
    public UsuarioDTO mapToDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setClave(usuario.getClave());
        usuarioDTO.setNombreUsuario(usuario.getNombreUsuario());
        usuarioDTO.setRuta_imagen(usuario.getRuta_imagen());
        usuarioDTO.setUrl_imagen(usuario.getUrl_imagen());

        usuarioDTO.setCedula(usuario.getPersona().getCedula());
        usuarioDTO.setEstado(usuario.getEstado());
        usuarioDTO.setRol(usuario.getRol().getNombre());
        return usuarioDTO;
    }

    @Override
    public List findAll() {
        return usuarioRepository.findAll().stream()
                .peek(u -> u.setUrl_imagen(u.getRuta_imagen() == null ? null : s3Service.getObjectUrl(u.getRuta_imagen())))
                .map(e -> mapToDTO(e))
                .collect(Collectors.toList());
    }

    public UsuarioDTO findByIdToDTO(long id) {
        return mapToDTO(usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id", id)));
    }

    public Usuario findByUsername(String username) {
        return usuarioRepository.findBynombreUsuario(username).orElseThrow(() -> new ResourceNotFoundException("usuario:", username));
    }

    public Usuario findByUsername2(String username) {
        return usuarioRepository.findBynombreUsuario(username)
                .map(u -> {
                    u.setUrl_imagen(u.getRuta_imagen() == null ? null : s3Service.getObjectUrl(u.getRuta_imagen()));
                    return u;
                })
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con nombre de usuario: " + username));
    }

    public Usuario findByUsernameAndClave(String username, String clave) {
        return usuarioRepository.findBynombreUsuarioAndClave(username, clave).orElseThrow(() -> new ResourceNotFoundException("usario:", username));
    }

    public Usuario update(long id, UsuarioDTO usuarioDTO) {

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id", id));
        if (!usuario.getNombreUsuario().equalsIgnoreCase(usuarioDTO.getNombreUsuario()) && usuarioRepository.existsBynombreUsuario(usuarioDTO.getNombreUsuario())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "El usuario ya se encuentra en otro registro");
        }
        Rol rol = rolRepository.findByNombre(usuarioDTO.getRol())
                .orElseThrow(() -> new ResourceNotFoundException("nombre:", usuarioDTO.getRol()));
        Persona p = personaRepository.findBycedula(usuarioDTO.getCedula())
                .orElseThrow(() -> new ResourceNotFoundException("cedula:", usuarioDTO.getCedula()));
        ;

        usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
       /* if (!usuarioDTO.getClave().isEmpty()) {
            usuario.setClave(passwordEncoder.encode(usuarioDTO.getClave()));
        }*/

        usuario.setUrl_imagen(usuarioDTO.getUrl_imagen());
        usuario.setRuta_imagen(usuarioDTO.getRuta_imagen());

        usuario.setEstado(usuarioDTO.isEstado());
        usuario.setPersona(p);
        usuario.setRol(rol);

        return usuarioRepository.save(usuario);
    }

    public Usuario updatePhoto(long id, String ruta) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User NOT FOUND", id));
        usuario.setRuta_imagen(ruta);
        usuario.setUrl_imagen(s3Service.getObjectUrl(ruta));
        return usuarioRepository.save(usuario);
    }

    public Usuario updatePassword(long id, String newPassword) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No encontrado el id: ", id));

        if (!newPassword.isEmpty()) {
            String code = passwordEncoder.encode(newPassword);
            usuario.setClave(code);
        }

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

    @Transactional
    public void registerUserAndPerson(Persona persona, RegistroDTO usuarioDTO) {

        UsuarioDTO user = new UsuarioDTO();
        user.setNombreUsuario(usuarioDTO.getNombreUsuario());
        user.setCedula(persona.getCedula());
        user.setClave(usuarioDTO.getClave());
        user.setRol(usuarioDTO.getRol());
        user.setEstado(usuarioDTO.isEstado());
        user.setRuta_imagen(usuarioDTO.getRutaImagen());
        user.setUrl_imagen(usuarioDTO.getUrlImagen());

        save(user);
    }
}
