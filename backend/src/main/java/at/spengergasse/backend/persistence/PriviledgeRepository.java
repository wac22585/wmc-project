package at.spengergasse.backend.persistence;

import at.spengergasse.backend.model.CountryNumber;
import at.spengergasse.backend.model.PriviledgeRole;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface PriviledgeRepository extends Repository<PriviledgeRole, Long> {

    void save(PriviledgeRole priviledgeRole);

    PriviledgeRole findById(Long id);

    PriviledgeRole findByName(String name);

    List<PriviledgeRole> findAll();
}
