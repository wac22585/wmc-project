package at.spengergasse.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.List;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "roles")
public class Role extends AbstractPersistable<Long>
{
    @Enumerated(EnumType.STRING)
    private ERoles name;
    @OneToMany(mappedBy = "role")
    private List<UserRole> userRoles;
}
