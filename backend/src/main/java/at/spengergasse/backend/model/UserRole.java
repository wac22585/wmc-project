package at.spengergasse.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "userroles")
public class UserRole
{
    @Id
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;
    @Id
    @ManyToOne
    private Role role;
}
