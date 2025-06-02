package sitema.monitorizacion.Api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import sitema.monitorizacion.Api.model.Role;
import sitema.monitorizacion.Api.model.User;
import sitema.monitorizacion.Api.repository.RoleRepository;
import sitema.monitorizacion.Api.repository.UserRepository;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Crear roles si no existen
            if (roleRepository.count() == 0) {
                Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
                Role nurseRole = roleRepository.save(Role.builder().name("NURSE").build());
                
                // Crear usuario admin por defecto
                if (userRepository.count() == 0) {
                    User adminUser = User.builder()
                            .name("Administrador")
                            .email("admin@example.com")
                            .password(passwordEncoder.encode("admin123"))
                            .role(adminRole)
                            .build();
                    
                    User nurseUser = User.builder()
                            .name("Enfermera")
                            .email("nurse@example.com")
                            .password(passwordEncoder.encode("nurse123"))
                            .role(nurseRole)
                            .build();
                    
                    userRepository.save(adminUser);
                    userRepository.save(nurseUser);
                }
            }
        };
    }
} 