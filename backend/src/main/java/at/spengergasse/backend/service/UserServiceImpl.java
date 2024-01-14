package at.spengergasse.backend.service;

import at.spengergasse.backend.dto.UserRequest;
import at.spengergasse.backend.dto.UserResponse;
import at.spengergasse.backend.model.*;
import at.spengergasse.backend.persistence.RoleRepository;
import at.spengergasse.backend.persistence.UserRepository;
import at.spengergasse.backend.persistence.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRoleRepository userRoleRepository;

    ModelMapper modelMapper = new ModelMapper();



    @Override
    public void saveUser(UserRequest userRequest) {
        if(userRequest.getEmail() == null){
            throw new RuntimeException("Parameter username is not found in request..!!");
        } else if(userRequest.getPasswordHash() == null){
            throw new RuntimeException("Parameter password is not found in request..!!");
        }

        User savedUser = null;

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = userRequest.getPasswordHash();
        String encodedPassword = encoder.encode(rawPassword);

        User user = User.builder()
                .firstname(userRequest.getFirstname())
                .lastname(userRequest.getLastname())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .created(LocalDateTime.now())
                .isDeleted(false)
                .birthdate(userRequest.getBirthdate())
                .priviledgeRoles(userRequest.getPriviledgeRoles())
                .build();
        user.setPasswordHash(encodedPassword);
        user.setId(UUID.randomUUID());
        savedUser = userRepository.save(user);
        for(Long roleId : userRequest.getRoles()) {
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

    @Override
    public UserResponse getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        String usernameFromAccessToken = userDetail.getUsername();
        User user = userRepository.findByEmail(usernameFromAccessToken);
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        return userResponse;
    }

    @Override
    public List<UserResponse> getAllUser() {
        List<User> users = (List<User>) userRepository.findAll();
        Type setOfDTOsType = new TypeToken<List<UserResponse>>(){}.getType();
        List<UserResponse> userResponses = modelMapper.map(users, setOfDTOsType);
        return userResponses;
    }


}