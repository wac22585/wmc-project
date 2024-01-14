package at.spengergasse.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User
{
    @Id
    @UuidGenerator
    private UUID id;
    private String firstname;
    private String lastname;
    @Column(unique = true)
    private String email;
    private String passwordHash;
    private String phoneNumber;
    private Date birthdate;
    private boolean isDeleted;
    private LocalDateTime created;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private CountryNumber countryNumber;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<UserRole> roles;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<PriviledgeRole> priviledgeRoles = new HashSet<>();

    public List<String> getRolesString()
    {
        return this.roles.stream()
                .map(userRole -> userRole.getRole().getName().toString()).collect(Collectors.toList());
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}