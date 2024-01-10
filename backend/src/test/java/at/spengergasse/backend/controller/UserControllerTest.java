package at.spengergasse.backend.controller;

import at.spengergasse.backend.model.Role;
import at.spengergasse.backend.model.User;
import at.spengergasse.backend.model.UserRole;
import at.spengergasse.backend.model.UserRoleId;
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
                .firstname("Emil")
                .lastname("Wache")
                .email("eow@gmx.at")
                .passwordHash(passwordEncoder.encode("newpassword"))
                .phoneNumber("123456789")
                .created(LocalDateTime.now())
                .isDeleted(false)
                .birthdate(null)
                .build();

        List<Long> roleIds = List.of(1L, 2L, 3L);

        userRepository.save(user);

        for(Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId);
            if(role != null) {
                UserRole userRole = UserRole.builder()
                        .id(new UserRoleId(user.getId(), roleId))
                        .user(user)
                        .role(role)
                        .build();
                userRoleRepository.save(userRole);
            }
        }

        User user1 = User.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .passwordHash(passwordEncoder.encode("secret"))
                .phoneNumber("238734124")
                .created(LocalDateTime.now())
                .isDeleted(false)
                .birthdate(null)
                .build();

        List<Long> roleIds1 = List.of(2L, 3L, 4L);

        userRepository.save(user1);

        for(Long roleId : roleIds1) {
            Role role = roleRepository.findById(roleId);
            if(role != null) {
                UserRole userRole = UserRole.builder()
                        .id(new UserRoleId(user1.getId(), roleId))
                        .user(user1)
                        .role(role)
                        .build();
                userRoleRepository.save(userRole);
            }
        }

        User user2 = User.builder()
                .firstname("Jane")
                .lastname("Doe")
                .email("jane.doe@gmail.com")
                .passwordHash(passwordEncoder.encode("password123"))
                .phoneNumber("3947356302")
                .created(LocalDateTime.now())
                .isDeleted(false)
                .birthdate(null)
                .build();

        List<Long> roleIds2 = List.of(4L, 5L, 2L);

        userRepository.save(user2);

        for(Long roleId : roleIds2) {
            Role role = roleRepository.findById(roleId);
            if(role != null) {
                UserRole userRole = UserRole.builder()
                        .id(new UserRoleId(user2.getId(), roleId))
                        .user(user2)
                        .role(role)
                        .build();
                userRoleRepository.save(userRole);
            }
        }

        User user4 = User.builder()
                .firstname("Alice")
                .lastname("Smith")
                .email("alice.smith@example.com")
                .passwordHash(passwordEncoder.encode("alice123"))
                .phoneNumber("984730123")
                .created(LocalDateTime.now())
                .isDeleted(false)
                .birthdate(null)
                .build();

        List<Long> roleIds4 = List.of(1L, 3L, 5L);

        userRepository.save(user4);

        for(Long roleId : roleIds4) {
            Role role = roleRepository.findById(roleId);
            if(role != null) {
                UserRole userRole = UserRole.builder()
                        .id(new UserRoleId(user4.getId(), roleId))
                        .user(user4)
                        .role(role)
                        .build();
                userRoleRepository.save(userRole);
            }
        }

        User user5 = User.builder()
                .firstname("Bob")
                .lastname("Johnson")
                .email("bob.johnson@example.com")
                .passwordHash(passwordEncoder.encode("bobpass"))
                .phoneNumber("987654321")
                .created(LocalDateTime.now())
                .isDeleted(false)
                .birthdate(null)
                .build();

        List<Long> roleIds5 = List.of(2L, 4L, 6L);

        userRepository.save(user5);

        for(Long roleId : roleIds5) {
            Role role = roleRepository.findById(roleId);
            if(role != null) {
                UserRole userRole = UserRole.builder()
                        .id(new UserRoleId(user5.getId(), roleId))
                        .user(user5)
                        .role(role)
                        .build();
                userRoleRepository.save(userRole);
            }
        }

        User user6 = User.builder()
                .firstname("Charlotte")
                .lastname("Brown")
                .email("charlotte.brown@example.com")
                .passwordHash(passwordEncoder.encode("charlotte2024"))
                .phoneNumber("125673490")
                .created(LocalDateTime.now())
                .isDeleted(false)
                .birthdate(null)
                .build();

        List<Long> roleIds6 = List.of(1L, 4L, 6L);

        userRepository.save(user6);

        for(Long roleId : roleIds6) {
            Role role = roleRepository.findById(roleId);
            if(role != null) {
                UserRole userRole = UserRole.builder()
                        .id(new UserRoleId(user6.getId(), roleId))
                        .user(user6)
                        .role(role)
                        .build();
                userRoleRepository.save(userRole);
            }
        }

        User user7 = User.builder()
                .firstname("David")
                .lastname("Miller")
                .email("david.miller@example.com")
                .passwordHash(passwordEncoder.encode("davidpass123"))
                .phoneNumber("987654320")
                .created(LocalDateTime.now())
                .isDeleted(false)
                .birthdate(null)
                .build();

        List<Long> roleIds7 = List.of(2L, 3L, 5L);

        userRepository.save(user7);

        for(Long roleId : roleIds7) {
            Role role = roleRepository.findById(roleId);
            if(role != null) {
                UserRole userRole = UserRole.builder()
                        .id(new UserRoleId(user7.getId(), roleId))
                        .user(user7)
                        .role(role)
                        .build();
                userRoleRepository.save(userRole);
            }
        }

    }
}