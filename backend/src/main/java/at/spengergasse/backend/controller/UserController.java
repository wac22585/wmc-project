package at.spengergasse.backend.controller;

import at.spengergasse.backend.dto.UserDTO;
import at.spengergasse.backend.model.*;
import at.spengergasse.backend.persistence.RoleRepository;
import at.spengergasse.backend.persistence.UserRepository;
import at.spengergasse.backend.persistence.UserRoleRepository;
import at.spengergasse.backend.service.JwtService;
import at.spengergasse.backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public @ResponseBody Iterable<UserDTO> allUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(u -> !u.isDeleted())
                .map(user -> new UserDTO(user.getId(), user.getEmail(), user.getPhoneNumber(), user.getFirstname(), user.getLastname(),
                        user.getCreated(), user.getBirthdate(), user.getRoles()))
                .collect(Collectors.toList());
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletResponse response)
    {
        try {
            UserDTO userDTO = userService.login(email, password);
            if (userDTO != null) {
                String jwtToken = jwtService.GenerateToken(email, userDTO.roles());
                ResponseCookie jwtCookie = ResponseCookie.from("accessToken", jwtToken)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(86400)
                        .build();

                response.addHeader("Set-Cookie", jwtCookie.toString());
                return ResponseEntity.ok(userDTO);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
            }
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie jwtCookie = ResponseCookie.from("accessToken", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", jwtCookie.toString());
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/add")
    public ResponseEntity<?> createUser(@RequestParam("firstname") String firstname,
                                        @RequestParam("lastname") String lastname,
                                        @RequestParam("email") String email,
                                        @RequestParam("password") String password,
                                        @RequestParam("number") String phoneNumber,
                                        @RequestParam(value = "birthdate", required = false) Date birthdate,
                                        @RequestParam(value = "roles") List<Long> roleIds)
    {
        try {
            String result = userService.createUser(firstname, lastname, email, password, phoneNumber, birthdate, roleIds);
            return ResponseEntity.ok(result);
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e) {
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
                user.setDeleted(LocalDateTime.now());
                userRepository.save(user);
                return new ResponseEntity("OK", HttpStatusCode.valueOf(200));
            } catch (Exception e)
            {
                return new ResponseEntity("Error deleting user. Please try again.", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity("User could not be found. Please try again.", HttpStatus.NOT_FOUND);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<UserDTO> get(@PathVariable final UUID id) {
        User user = userRepository.findById(id);
        if(user != null) {
            UserDTO userDTO = new UserDTO(user.getId(), user.getEmail(), user.getPhoneNumber(), user.getFirstname(), user.getLastname(), user.getCreated(), user.getBirthdate(), user.getRoles());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/validateToken")
    public ResponseEntity<?> validateToken(@CookieValue(name = "accessToken", required = false) String jwtToken) {
       if(jwtToken == null || jwtToken.isEmpty()) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
       }

       try {
            if(jwtService.validateToken(jwtToken)) {
                List<String> roles = jwtService.extractRoles(jwtToken);
                boolean isAdmin = roles.contains("ADMINISTRATOR");
                Map<String, Object> response = new HashMap<>();
                response.put("isAdmin", isAdmin);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Error: JWT token is invalid or expired.");

            }
       } catch(Exception e) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Error: An exception occurred during token validation.");
       }
    }
}
