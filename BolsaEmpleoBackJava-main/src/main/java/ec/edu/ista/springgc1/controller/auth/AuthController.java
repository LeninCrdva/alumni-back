package ec.edu.ista.springgc1.controller.auth;

import ec.edu.ista.springgc1.exception.AppException;
import ec.edu.ista.springgc1.model.dto.LoginDTO;
import ec.edu.ista.springgc1.model.dto.RecoveryDTO;
import ec.edu.ista.springgc1.model.dto.UsuarioDTO;
import ec.edu.ista.springgc1.model.entity.RecoveryToken;
import ec.edu.ista.springgc1.model.entity.Usuario;
import ec.edu.ista.springgc1.security.jwt.JwtAuthResponse;
import ec.edu.ista.springgc1.security.jwt.JwtTokenProvider;
import ec.edu.ista.springgc1.service.impl.RecoveryTokenServiceImpl;
import ec.edu.ista.springgc1.service.impl.UsuarioServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private RecoveryTokenServiceImpl recoveryTokenService;
    
    @Autowired
    private UsuarioServiceImpl userService;

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioService.findByUsername(userDetails.getUsername());

        if (!usuario.getEstado()) {
            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }

        String token = jwtTokenProvider.generateToken(authentication, generateExtraClaims(userDetails));

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse(
                token, usuario.getId(), usuario.getNombreUsuario(),userDetails.getAuthorities()
        );

        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UsuarioDTO usuarioDTO){

        if (usuarioService.existsByUsername(usuarioDTO.getNombreUsuario())){
            throw new AppException(HttpStatus.BAD_REQUEST,"Ya se encuentra registrado el nombre de usuario");
        }

        usuarioService.save(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("mensaje","Usuario Registrado"));
    }

    @PreAuthorize("permitAll()")
    @PutMapping("/login/recovery-password")
    public ResponseEntity<?> recoveryPassword(@RequestBody RecoveryDTO recovery){
    	RecoveryToken bypassToken = recoveryTokenService.findByToken(recovery.getToken());
    	
    	if(bypassToken == null) {
    		throw new AppException(HttpStatus.NOT_FOUND,"No se ha encontrado el token ingresado");
    	} 
    	
    	if (!bypassToken.getActive()) {
    		throw new AppException(HttpStatus.FORBIDDEN,"El token ingresado no está activo");
    	}
    	
    	if(bypassToken.getExpiration().compareTo(new Date()) < 0 ) {
    		deactivateTokenAndSave(bypassToken);
    		throw new AppException(HttpStatus.GONE,"El token ya ha expirado");
    	}
    	
    	deactivateTokenAndSave(bypassToken);
    	
    	userService.updatePassword(bypassToken.getUsuario().getId(), recovery.getNewPassword());
    	
    	return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "Se ha actualizado la contraseña correctamente"));
    }
    
    private void deactivateTokenAndSave(RecoveryToken token) {
        token.setActive(Boolean.FALSE);
        recoveryTokenService.save(token);
    }

    private Map<String, Object> generateExtraClaims(UserDetails user) {
        Map<String, Object> extraClaims = new HashMap<>();

        List<String> roles = user.getAuthorities().stream()
                .map(authority -> "ROLE_" + authority.getAuthority())
                .collect(Collectors.toList());

        extraClaims.put("role", roles);

        return extraClaims;
    }
}
