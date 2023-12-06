package at.spengergasse.backend.controller;

import at.spengergasse.backend.model.User;
import at.spengergasse.backend.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController
{
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/hello")
    public String helloworld()
    {
        return "Hello world";
    }

    @GetMapping("/all")
    public @ResponseBody Iterable<User> allUsers()
    {
        return userRepository.findAll();
    }
}
