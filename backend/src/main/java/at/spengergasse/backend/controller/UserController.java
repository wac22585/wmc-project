package at.spengergasse.backend.controller;

import at.spengergasse.backend.dto.UserDTO;
import at.spengergasse.backend.dto.UserRequest;
import at.spengergasse.backend.model.*;
import at.spengergasse.backend.persistence.UserRepository;
import at.spengergasse.backend.service.JwtService;
import at.spengergasse.backend.service.RefreshTokenService;
import at.spengergasse.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    RefreshTokenService refreshTokenService;


    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/all")
    public @ResponseBody Iterable<UserDTO> allUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(u -> !u.isDeleted())
                .map(user -> new UserDTO(user.getId(), user.getEmail(), user.getPhoneNumber(), user.getFirstname(), user.getLastname(),
                        user.getCreated(), user.getBirthdate(), user.getRolesString()))
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public ResponseEntity<?> createUser(@ModelAttribute UserRequest userRequest)
    {
        try {
            userService.saveUser(userRequest);
            return ResponseEntity.ok("Added user");
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body("User could not be added. Please try again.");
        }
    }

    @PutMapping("update/{id}")
    public @ResponseStatus ResponseEntity update(@PathVariable final UUID id, @RequestBody UserDTO userDTO) {
        try {
            User user = userRepository.findById(id);
            if(user != null) {
                user.setFirstname(userDTO.firstname());
                user.setLastname(userDTO.lastname());
                user.setEmail(userDTO.email());
                user.setPhoneNumber(userDTO.phoneNumber());
                user.setBirthdate(userDTO.birthdate());
            /*List<UserRole> userRoles = new ArrayList<>();
            for (String roleName : userDTO.roles()) {
                try {
                    ERoles roleEnum = ERoles.valueOf(roleName);
                    Role role = roleRepository.findByName(roleEnum);

                    UserRole userRole = UserRole.builder()
                            .user(user)
                            .role(role)
                            .build();

                    userRoles.add(userRole);
                } catch (IllegalArgumentException e) {
                    return new ResponseEntity("Error", HttpStatus.BAD_REQUEST);
                }
            }
            user.setRoles(userRoles);*/
                userRepository.save(user);
                return new ResponseEntity("OK", HttpStatusCode.valueOf(200));
            }
            return new ResponseEntity("User could not be found. Please try again.", HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity("Something went wrong. Please try again.", HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("delete/{id}")
    public @ResponseStatus ResponseEntity delete(@PathVariable final UUID id) {
        User user = userRepository.findById(id);
        if(user != null)
        {
            try
            {
                user.setIsDeleted(true);
                //user.setDeleted(LocalDateTime.now());
                userRepository.save(user);
                return new ResponseEntity("OK", HttpStatusCode.valueOf(200));
            } catch (Exception e)
            {
                return new ResponseEntity("Error deleting user. Please try again.", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity("User could not be found. Please try again.", HttpStatus.NOT_FOUND);
    }

    @GetMapping("get/{email}")
    public ResponseEntity<UserDTO> get(@PathVariable final String email) {
        User user = userRepository.findByEmail(email);
        if(user != null) {
            UserDTO userDTO = new UserDTO(user.getId(), user.getEmail(),
                    user.getPhoneNumber(), user.getFirstname(), user.getLastname(),
                    user.getCreated(), user.getBirthdate(), user.getRolesString());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}