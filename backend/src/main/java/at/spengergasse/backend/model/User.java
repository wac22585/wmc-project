package at.spengergasse.backend.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends AbstractPersistable<Long>
{
    private String firstname;
    private String lastname;
    private String email;
    private String passwordHash;
    private long phoneNumber;
    private boolean isDeleted;
    private LocalDateTime created;
    private LocalDateTime deleted;
    private String userImage;
    private String authToken;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private CountryNumber countryNumber;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    private List<UserRole> roles;

    public void setPassword(String password) {
        this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyPassword(String password) {
        return BCrypt.checkpw(password, this.passwordHash);
    }
}