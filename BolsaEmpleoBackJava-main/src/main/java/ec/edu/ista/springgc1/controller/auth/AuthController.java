package ec.edu.ista.springgc1.controller.auth;

import ec.edu.ista.springgc1.exception.AppException;
import ec.edu.ista.springgc1.model.dto.LoginDTO;
import ec.edu.ista.springgc1.model.dto.UsuarioDTO;
import ec.edu.ista.springgc1.model.entity.Usuario;
import ec.edu.ista.springgc1.security.jwt.JwtAuthResponse;
import ec.edu.ista.springgc1.security.jwt.JwtTokenProvider;
import ec.edu.ista.springgc1.service.impl.UsuarioServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioService.findByUsername(userDetails.getUsername());

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse(
                token, usuario.getId(), usuario.getNombreUsuario(),userDetails.getAuthorities()
        );

        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UsuarioDTO usuarioDTO){

        if (usuarioService.existsByUsername(usuarioDTO.getNombreUsuario())){
            throw new AppException(HttpStatus.BAD_REQUEST,"Ya se encuentra registrado el nombre de usuario");
        }

        usuarioService.save(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("mensaje","Usuario Registrado"));
    }
}
