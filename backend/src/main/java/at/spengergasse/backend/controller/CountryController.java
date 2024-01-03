package at.spengergasse.backend.controller;

import at.spengergasse.backend.model.CountryNumber;
import at.spengergasse.backend.persistence.CountryNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@CrossOrigin(origins = "http://localhost:3000")
public class CountryController {

    @Autowired
    private CountryNumberRepository countryNumberRepository;

    @GetMapping("/all")
    public List<CountryNumber> getCountryCodes(){
        return countryNumberRepository.findAll();
    }
}
