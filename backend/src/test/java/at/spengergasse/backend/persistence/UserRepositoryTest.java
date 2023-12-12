package at.spengergasse.backend.persistence;

import at.spengergasse.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest
{
    @Autowired
    private UserRepository userRepository;


    @Test
    void verifySaveUser()
    {
        var user = User.builder()
                .firstname("Max")
                .lastname("Mustermann")
                .email("max@gmail.com")
                .build();
        assertThat(user.getId()).isNull();
        userRepository.save(user);
        assertThat(user.getId()).isNotNull();
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findByName() {
    }
}