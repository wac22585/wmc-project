package at.spengergasse.backend.persistence;

import at.spengergasse.backend.model.User;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface UserRepository extends Repository<User, Long>
{
    void save(User user);
    User findById(Long id);
    User findByEmail(String email);
    List<User> findAll();
    User findByAuthToken(String authToken);
}
