package at.spengergasse.backend.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest
{

    @Test
    void verifyAlive()
    {
        assertThat(true).isTrue();
    }
}