package ec.edu.ista.springgc1.repository;

import ec.edu.ista.springgc1.model.entity.Usuario;
import ec.edu.ista.springgc1.repository.generic.GenericRepository;

import java.util.Optional;

public interface UsuarioRepository extends GenericRepository<Usuario> {

    public Optional<Usuario> findBynombreUsuario(String username);
    public Optional<Usuario> findBynombreUsuarioAndClave(String username, String clave);
    public Boolean existsBynombreUsuario(String username);
}
