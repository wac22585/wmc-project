package at.spengergasse.backend.controller;

import at.spengergasse.backend.model.Role;
import at.spengergasse.backend.persistence.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/roles")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/all")
    public @ResponseBody Iterable<Role> allRoles() {
        return roleRepository.findAll();
    }

}
