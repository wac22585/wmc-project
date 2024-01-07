package at.spengergasse.backend.controller;

import at.spengergasse.backend.model.Role;
import at.spengergasse.backend.model.User;
import at.spengergasse.backend.model.UserRole;
import at.spengergasse.backend.persistence.RoleRepository;
import at.spengergasse.backend.persistence.UserRepository;
import at.spengergasse.backend.persistence.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void createUser()
    {
        User user = User.builder()
                .firstname("Max")
                .lastname("Mustermann")
                .email("max@gmail.com")
                .passwordHash(passwordEncoder.encode("password"))
                .phoneNumber(0)
                .created(LocalDateTime.now())
                .isDeleted(false)
                .birthdate(null)
                .build();

        List<Long> roleIds = List.of(2L, 3L);

        userRepository.save(user);

        for(Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId);
            if(role != null) {
                UserRole userRole = UserRole.builder()
                        .user(user)
                        .role(role)
                        .build();
                userRoleRepository.save(userRole);
            }
        }
    }
}