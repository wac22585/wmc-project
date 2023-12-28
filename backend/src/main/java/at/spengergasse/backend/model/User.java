package at.spengergasse.backend.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.Date;
import java.util.List;

@Entity
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
    private String password;
    private long phoneNumber;
    private boolean isDeleted;
    private Date created;
    private Date deleted;
    private String userImage;
    private String authToken;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private CountryNumber countryNumber;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    private List<UserRole> roles;

    public void setDeleted(boolean deleted)
    {
        this.isDeleted = deleted;
    }
}