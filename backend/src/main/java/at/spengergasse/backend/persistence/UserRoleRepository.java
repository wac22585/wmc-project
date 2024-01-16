package at.spengergasse.backend.persistence;

import at.spengergasse.backend.model.User;
import at.spengergasse.backend.model.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface UserRoleRepository extends Repository<UserRole, Long>
{
    void save(UserRole userRole);
    @Modifying
    @Transactional
    @Query("DELETE FROM UserRole ur WHERE ur.user = :user")
    void deleteByUser(@Param("user") User user);
}
