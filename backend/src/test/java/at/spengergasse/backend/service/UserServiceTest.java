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
                .firstname("Jane")
                .lastname("Doe")
                .email("jane.doe@gmail.com")
                .passwordHash(passwordEncoder.encode("password"))
                .phoneNumber(String.valueOf(145683333L))
                .created(LocalDateTime.now())
                .isDeleted(false)
                .birthdate(null)
                .priviledgeRoles(Set.of((new PriviledgeRole(2, "USER"))))
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

        User user1 = User.builder()
                .firstname("Adam")
                .lastname("Anderson")
                .email("adam.anderson@gmail.com")
                .passwordHash(passwordEncoder.encode("password"))
                .phoneNumber(String.valueOf(145683333L))
                .created(LocalDateTime.now())
                .isDeleted(false)
                .birthdate(null)
                .priviledgeRoles(Set.of((new PriviledgeRole(2, "USER"))))
                .build();

        userRepository.save(user1);

        List<Long> roleIds1 = List.of(1L, 4L, 6L);
        for(Long roleId : roleIds1) {
            Role role = roleRepository.findById(roleId);
            if(role != null) {
                UserRoleId userRoleId = new UserRoleId(user1.getId(), roleId);
                UserRole userRole = UserRole.builder()
                        .id(new UserRoleId(user1.getId(), roleId))
                        .user(user1)
                        .role(role)
                        .build();
                userRoleRepository.save(userRole);
            }
        }

        User user2 = User.builder()
                .firstname("Julia")
                .lastname("Martinez")
                .email("julia.martinez@gmail.com")
                .passwordHash(passwordEncoder.encode("password"))
                .phoneNumber(String.valueOf(145683333L))
                .created(LocalDateTime.now())
                .isDeleted(false)
                .birthdate(null)
                .priviledgeRoles(Set.of((new PriviledgeRole(2, "USER"))))
                .build();

        userRepository.save(user2);

        List<Long> roleIds2 = List.of(6L);
        for(Long roleId : roleIds2) {
            Role role = roleRepository.findById(roleId);
            if(role != null) {
                UserRoleId userRoleId = new UserRoleId(user2.getId(), roleId);
                UserRole userRole = UserRole.builder()
                        .id(new UserRoleId(user2.getId(), roleId))
                        .user(user2)
                        .role(role)
                        .build();
                userRoleRepository.save(userRole);
            }
        }

        User user3 = User.builder()
                .firstname("Marcus")
                .lastname("Taylor")
                .email("marcus.taylor@gmail.com")
                .passwordHash(passwordEncoder.encode("password"))
                .phoneNumber(String.valueOf(145683333L))
                .created(LocalDateTime.now())
                .isDeleted(false)
                .birthdate(null)
                .priviledgeRoles(Set.of((new PriviledgeRole(2, "USER"))))
                .build();

        userRepository.save(user3);

        List<Long> roleIds3 = List.of(3L);
        for(Long roleId : roleIds3) {
            Role role = roleRepository.findById(roleId);
            if(role != null) {
                UserRoleId userRoleId = new UserRoleId(user3.getId(), roleId);
                UserRole userRole = UserRole.builder()
                        .id(new UserRoleId(user3.getId(), roleId))
                        .user(user3)
                        .role(role)
                        .build();
                userRoleRepository.save(userRole);
            }
        }

        User user4 = User.builder()
                .firstname("Benjamin")
                .lastname("Walker")
                .email("benjamin.walker@gmail.com")
                .passwordHash(passwordEncoder.encode("password"))
                .phoneNumber(String.valueOf(145683333L))
                .created(LocalDateTime.now())
                .isDeleted(false)
                .birthdate(null)
                .priviledgeRoles(Set.of((new PriviledgeRole(2, "USER"))))
                .build();

        userRepository.save(user4);

        List<Long> roleIds4 = List.of(2L, 3L, 4L);
        for(Long roleId : roleIds4) {
            Role role = roleRepository.findById(roleId);
            if(role != null) {
                UserRoleId userRoleId = new UserRoleId(user4.getId(), roleId);
                UserRole userRole = UserRole.builder()
                        .id(new UserRoleId(user4.getId(), roleId))
                        .user(user4)
                        .role(role)
                        .build();
                userRoleRepository.save(userRole);
            }
        }
    }
}