package at.spengergasse.backend.dto;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserDTO(UUID id, String email, String firstname,
                      String lastname, LocalDateTime created, List<String> roles) { }