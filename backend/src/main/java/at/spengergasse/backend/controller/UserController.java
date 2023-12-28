package at.spengergasse.backend.controller;

import at.spengergasse.backend.dto.UserDTO;
import at.spengergasse.backend.model.User;
import at.spengergasse.backend.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController
{
    @Autowired
    private UserRepository userRepository;

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
       if(user == null || !user.getPassword().equals(password))
       {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
       }

       String authToken = generateAuthToken();

       Map<String, Object> response = new HashMap<>();
       response.put("token", authToken);
       response.put("user", user);

       return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public @ResponseStatus ResponseEntity createUser(@RequestBody User user)
    {
        try
        {
            userRepository.save(user);
            return new ResponseEntity("OK", HttpStatus.CREATED);
        } catch (Exception e)
        {
            return new ResponseEntity("Error", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("delete/{id}")
    public @ResponseStatus ResponseEntity delete(@PathVariable final Long id)
    {
        User user = userRepository.findById(id);
        if(user != null)
        {
            try
            {
                user.setDeleted(true);
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
