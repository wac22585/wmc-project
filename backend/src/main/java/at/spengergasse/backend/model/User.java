package at.spengergasse.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.AbstractPersistable;

import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
    private String email;
    private String passwordHash;
    private long phoneNumber;
    private Date birthdate;
    private boolean isDeleted;
    private LocalDateTime created;
    private LocalDateTime deleted;
    private String userImage;
    private String authToken;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private CountryNumber countryNumber;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    @JsonIgnore
    private List<UserRole> roles;

    public void setPassword(String password) {
        this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyPassword(String password) {
        return BCrypt.checkpw(password, this.passwordHash);
    }

    public List<String> getRoles()
    {
        return this.roles.stream()
                .map(userRole -> userRole.getRole().getName().toString()).collect(Collectors.toList());
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}