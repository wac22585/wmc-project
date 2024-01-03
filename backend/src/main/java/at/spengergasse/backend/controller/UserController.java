package at.spengergasse.backend.controller;

import at.spengergasse.backend.dto.UserDTO;
import at.spengergasse.backend.model.Role;
import at.spengergasse.backend.model.User;
import at.spengergasse.backend.model.UserRole;
import at.spengergasse.backend.model.UserRoleId;
import at.spengergasse.backend.persistence.RoleRepository;
import at.spengergasse.backend.persistence.UserRepository;
import at.spengergasse.backend.persistence.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @GetMapping("/all")
    public @ResponseBody Iterable<UserDTO> allUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(u -> !u.isDeleted())
                .map(user -> new UserDTO(user.getId(), user.getEmail(), user.getFirstname(), user.getLastname(),
                        user.getCreated(), user.getRoles()))
                .collect(Collectors.toList());
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam("email") String email, @RequestParam("password") String password)
    {
       if(email == null || email.isBlank()) return ResponseEntity.badRequest().body("Email is required.");
       if(password == null || password.isBlank()) return ResponseEntity.badRequest().body("Password is required.");

       User user = userRepository.findByEmail(email);
       if(user == null || !user.verifyPassword(password))
       {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
       }

       String authToken = generateAuthToken();

       user.setAuthToken(authToken);
       userRepository.save(user);

       UserDTO userDTO = new UserDTO(user.getId(), user.getEmail(), user.getFirstname(), user.getLastname(), user.getCreated(), user.getRoles());

       Map<String, Object> response = new HashMap<>();
       response.put("token", authToken);
       response.put("user", userDTO);

       return ResponseEntity.ok(response);
    }

    @PutMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam("authToken") String authToken) {
        User u = userRepository.findByAuthToken(authToken);
        if(u == null) return ResponseEntity.notFound().build();
        u.setAuthToken(null);
        userRepository.save(u);
        return ResponseEntity.ok("Logout successfull");
    }

    @GetMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestParam("authToken") String authToken) {
        User user = userRepository.findByAuthToken(authToken);
        if(user != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> createUser(@RequestParam("firstname") String firstname,
                                        @RequestParam("lastname") String lastname,
                                        @RequestParam("email") String email,
                                        @RequestParam("password") String password,
                                        @RequestParam("number") long phoneNumber,
                                        @RequestParam(value = "birthdate", required = false) Date birthdate,
                                        @RequestParam(value = "roles") List<Long> roleIds)
    {
        if(firstname == null || firstname.isBlank() || lastname == null || lastname.isBlank() || email == null ||
                email.isBlank() || password == null || password.isBlank() || roleIds.isEmpty()){
            return ResponseEntity.badRequest().body("Argument missing!");
        }

        User user = User.builder()
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .phoneNumber(phoneNumber)
                .created(LocalDateTime.now())
                .isDeleted(false)
                .userImage(null)
                .authToken(null)
                .deleted(null)
                .build();
        if(birthdate != null) user.setBirthdate(birthdate);
        user.setPassword(password);
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

        return ResponseEntity.ok("Added user successfully");
    }



    @PutMapping("delete/{id}")
    public @ResponseStatus ResponseEntity delete(@PathVariable final UUID id)
    {
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
                return new ResponseEntity("Error", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity("Not found", HttpStatus.NOT_FOUND);
    }

    public static String generateAuthToken() {
        int tokenLength = 32;

        byte[] randomBytes = new byte[tokenLength];
        new SecureRandom().nextBytes(randomBytes);

        String authToken = Base64.getEncoder().encodeToString(randomBytes);

        return authToken;
    }


}
