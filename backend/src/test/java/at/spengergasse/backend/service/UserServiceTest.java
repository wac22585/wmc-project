package at.spengergasse.backend.service;

import at.spengergasse.backend.model.*;
import at.spengergasse.backend.persistence.RoleRepository;
import at.spengergasse.backend.persistence.UserRepository;
import at.spengergasse.backend.persistence.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void createUser() {
        User user = User.builder()
                .firstname("test")
                .lastname("test")
                .email("test.test@gmail.com")
                .passwordHash(passwordEncoder.encode("password"))
                .phoneNumber(String.valueOf(145683333L))
                .created(LocalDateTime.now())
                .isDeleted(false)
                .birthdate(null)
                .priviledgeRoles(Set.of((new PriviledgeRole(1, "ADMIN"))))
                .build();

        userRepository.save(user);

        List<Long> roleIds = List.of(1L, 2L, 3L);
        for(Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId);
            if(role != null) {
                UserRoleId userRoleId = new UserRoleId(user.getId(), roleId);
                UserRole userRole = UserRole.builder()
                        .id(new UserRoleId(user.getId(), roleId))
                        .user(user)
                        .role(role)
                        .build();
                userRoleRepository.save(userRole);
            }
        }
    }
}