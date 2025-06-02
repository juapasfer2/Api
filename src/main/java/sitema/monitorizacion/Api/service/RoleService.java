package sitema.monitorizacion.Api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sitema.monitorizacion.Api.model.Role;
import sitema.monitorizacion.Api.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {
    
    private final RoleRepository roleRepository;
    
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));
    }
    
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con nombre: " + name));
    }
    
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }
    
    public Role updateRole(Long id, Role roleDetails) {
        Role role = getRoleById(id);
        role.setName(roleDetails.getName());
        return roleRepository.save(role);
    }
    
    public void deleteRole(Long id) {
        Role role = getRoleById(id);
        roleRepository.delete(role);
    }
} 