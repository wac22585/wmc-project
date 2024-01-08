package at.spengergasse.backend.dto;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record UserDTO(UUID id, String email, String phoneNumber, String firstname,
                      String lastname, LocalDateTime created, Date birthdate, List<String> roles) { }