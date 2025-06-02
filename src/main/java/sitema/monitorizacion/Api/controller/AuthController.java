package sitema.monitorizacion.Api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sitema.monitorizacion.Api.dto.AuthRequest;
import sitema.monitorizacion.Api.dto.AuthResponse;
import sitema.monitorizacion.Api.model.User;
import sitema.monitorizacion.Api.security.JwtService;
import sitema.monitorizacion.Api.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        if (authentication.isAuthenticated()) {
            User user = userService.getUserByEmail(request.getEmail());
            String token = jwtService.generateToken(
                    org.springframework.security.core.userdetails.User.builder()
                            .username(user.getEmail())
                            .password(user.getPassword())
                            .authorities("ROLE_" + user.getRole().getName())
                            .build()
            );
            
            return ResponseEntity.ok(AuthResponse.builder()
                    .token(token)
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole().getName())
                    .build());
        }
        
        return ResponseEntity.status(401).build();
    }
} 