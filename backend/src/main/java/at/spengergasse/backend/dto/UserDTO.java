package at.spengergasse.backend.dto;

import at.spengergasse.backend.model.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public record UserDTO(Long id, String email, String firstname,
                      String lastname, LocalDateTime created, List<String> roles) { }