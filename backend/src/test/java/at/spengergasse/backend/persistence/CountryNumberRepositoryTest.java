package at.spengergasse.backend.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CountryNumberRepositoryTest
{
    @Autowired
    private CountryNumberRepository countryNumberRepository;

    @Test
    void verifyCountryNumberRepository()
    {
        assertThat(countryNumberRepository).isNotNull().isInstanceOf(CountryNumberRepository.class);
    }


    @Test
    void save() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByName() {
    }

    @Test
    void findAll() {
    }
}