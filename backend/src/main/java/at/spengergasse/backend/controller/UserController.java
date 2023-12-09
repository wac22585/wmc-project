package at.spengergasse.backend.controller;

import at.spengergasse.backend.dto.UserDto;
import at.spengergasse.backend.model.User;
import at.spengergasse.backend.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3001")
public class UserController
{
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public @ResponseBody Iterable<User> allUsers()
    {
        return userRepository.findAll();
    }

    @GetMapping("/login")
    public boolean login(@RequestParam("email") String email, @RequestParam("password") String password)
    {
        if(email == null || password == null)
        {
            return false;
        }

        User user = userRepository.findByEmail(email);
        return user.getPassword().equals(password);
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
}
