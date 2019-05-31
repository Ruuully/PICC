package com.grokonez.jwtauthentication.controller;

import com.grokonez.jwtauthentication.model.Role;
import com.grokonez.jwtauthentication.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Collection;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/apir")
@PreAuthorize("hasAnyAuthority('GERER_ROLES')")

public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/roles")
    public Collection<Role> getRoles() {
        return roleService.findAll();
    }

    @GetMapping(value = "/role/{id}")
    public Object getRole(@PathVariable Long id) {
        return roleService.findById(id);
    }

    @DeleteMapping("/role/{id}")
    public boolean deleteRole(@PathVariable Long id) {
        Role role = roleService.getOne(id);
        roleService.delete(role);
        ;
        return true;
    }


    @PutMapping("/role")
    public Role UpdateRole(@RequestBody Role role) {
        return roleService.save(role);
    }

    @PostMapping("/role")
    @Transactional
    public Role CreateRole(@RequestBody Role role) {
        return roleService.save(role);
    }
}
