package at.spengergasse.backend.persistence;

import at.spengergasse.backend.model.UserRole;
import org.springframework.data.repository.Repository;

public interface UserRoleRepository extends Repository<UserRole, Long>
{
    void save(UserRole userRole);
}
