package ec.edu.ista.springgc1.service.impl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.edu.ista.springgc1.exception.ResourceNotFoundException;

import ec.edu.ista.springgc1.model.dto.SuperAdminDTO;

import ec.edu.ista.springgc1.model.entity.SuperAdmin;
import ec.edu.ista.springgc1.model.entity.Usuario;

import ec.edu.ista.springgc1.repository.SuperAdminRepository;
import ec.edu.ista.springgc1.repository.UsuarioRepository;

import ec.edu.ista.springgc1.service.generic.impl.GenericServiceImpl;
import ec.edu.ista.springgc1.service.map.Mapper;

@Service
public class SuperAdminServiceImpl extends GenericServiceImpl<SuperAdmin> implements Mapper<SuperAdmin, SuperAdminDTO> {

    @Autowired
    private SuperAdminRepository superadminrepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public SuperAdmin mapToEntity(SuperAdminDTO superadminDTO) {
        SuperAdmin superadmin = new SuperAdmin();

        Usuario usuario = usuarioRepository.findBynombreUsuario(superadminDTO.getUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("usuario", superadminDTO.getUsuario()));

        superadmin.setId(superadminDTO.getId());
        superadmin.setUsuario(usuario);
        superadmin.setEstado(true);


        return superadmin;
    }

    @Override
    public SuperAdminDTO mapToDTO(SuperAdmin superadmin) {
        SuperAdminDTO superadminDTO = new SuperAdminDTO();
        superadminDTO.setId(superadmin.getId());
        superadminDTO.setUsuario(superadmin.getUsuario().getNombreUsuario());
        superadminDTO.setEstado(true);

        return superadminDTO;
    }

    @Override
    public List findAll() {
        return superadminrepository.findAll()
                .stream()
                .map(c -> mapToDTO(c))
                .collect(Collectors.toList());
    }

    public SuperAdminDTO findByIdToDTO(Long id) {
        SuperAdmin estudiante = superadminrepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id", id));

        return mapToDTO(estudiante);
    }

    public SuperAdminDTO findByUsuario(long id_usuario) {

        SuperAdmin superadmin = superadminrepository.findByUsuario(id_usuario)
                .orElseThrow(() -> new ResourceNotFoundException("id_usuario", id_usuario));


        return mapToDTO(superadmin);
    }

    @Override
    public SuperAdmin save(Object entity) {

        return superadminrepository.save(mapToEntity((SuperAdminDTO) entity));
    }
}
