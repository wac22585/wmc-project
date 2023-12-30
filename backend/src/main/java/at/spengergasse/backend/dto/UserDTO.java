package at.spengergasse.backend.dto;


import java.time.LocalDateTime;
import java.util.List;

public record UserDTO(Long id, String email, String firstname,
                      String lastname, LocalDateTime created, List<String> roles) { }