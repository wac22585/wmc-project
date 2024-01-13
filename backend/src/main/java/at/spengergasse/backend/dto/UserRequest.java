package at.spengergasse.backend.dto;

import at.spengergasse.backend.model.CountryNumber;
import at.spengergasse.backend.model.PriviledgeRole;
import at.spengergasse.backend.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequest {

    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private String passwordHash;
    private String phoneNumber;
    private Date birthdate;
    private List<Long> roles;
    private Set<PriviledgeRole> priviledgeRoles;
}
