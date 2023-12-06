package at.spengergasse.backend.persistence;

import at.spengergasse.backend.model.CountryNumber;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface CountryNumberRepository extends Repository<CountryNumber, Long>
{
    void save(CountryNumber countryNumber);
    CountryNumber findById(Long id);
    CountryNumber findByName(String name);
    List<CountryNumber> findAll();
}
