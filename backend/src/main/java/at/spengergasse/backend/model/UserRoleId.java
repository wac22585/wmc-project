package at.spengergasse.backend.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class UserRoleId implements Serializable{
    private UUID userId;
    private Long roleId;

    public UserRoleId() {
    }

    public UserRoleId(UUID userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
