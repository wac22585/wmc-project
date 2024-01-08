package at.spengergasse.backend.service;

import at.spengergasse.backend.dto.UserDTO;
import at.spengergasse.backend.model.User;
import at.spengergasse.backend.model.Role;
import at.spengergasse.backend.model.UserRole;
import at.spengergasse.backend.model.UserRoleId;
import at.spengergasse.backend.persistence.UserRepository;
import at.spengergasse.backend.persistence.RoleRepository;
import at.spengergasse.backend.persistence.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String createUser(String firstname, String lastname, String email, String rawPassword,
                             String phoneNumber, Date birthdate, List<Long> roleIds) {
        if(firstname == null || firstname.isBlank() || lastname == null || lastname.isBlank() ||
                email == null || email.isBlank() || rawPassword == null || rawPassword.isBlank() ||
                roleIds.isEmpty()) {
            throw new IllegalArgumentException("Arguments are missing. Please try again.");
        }

        User user = User.builder()
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .phoneNumber(phoneNumber)
                .created(LocalDateTime.now())
                .isDeleted(false)
                .birthdate(birthdate)
                .build();

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

        return "Added user successfully";
    }

    public UserDTO login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            return new UserDTO(user.getId(), user.getEmail(), user.getPhoneNumber(), user.getFirstname(),
                    user.getLastname(), user.getCreated(), user.getBirthdate(), user.getRoles());
        }
        return null;
    }
}
