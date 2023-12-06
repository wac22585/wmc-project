package at.spengergasse.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends AbstractPersistable<Long>
{
    private String firstname;
    private String lastname;
    private String email;
    private long phoneNumber;
    private boolean isDeleted;
    private Date created;
    private Date deleted;
    private String userImage;
    @ManyToOne
    private CountryNumber countryNumber;
    @OneToMany
    private List<UserRole> roles;
}