package at.spengergasse.backend.controller;

import at.spengergasse.backend.model.User;
import at.spengergasse.backend.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserRepository userRepository;
    @Test
    void createUser()
    {
        User u = User.builder()
                .firstname("Max")
                .lastname("Mustermann")
                .email("mustermann@spengergasse.at")
                .build();
        u.setPassword("speng!max");
        userRepository.save(u);
    }
}