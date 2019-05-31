package com.grokonez.jwtauthentication.controller;

import com.grokonez.jwtauthentication.model.Privilege;
import com.grokonez.jwtauthentication.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Collection;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/apipri")
public class PrivilegeController {
    @Autowired
    private PrivilegeService privilegeService;

    @GetMapping("/privileges")
    public Collection<Privilege> getPrivileges() {
        return privilegeService.findAll();
    }

    @GetMapping(value = "/privilege/{id}")
    public Object getPrivilege(@PathVariable Long id) {
        return privilegeService.findById(id);
    }

    @DeleteMapping("/privilege/{id}")
    public boolean deletePrivilege(@PathVariable Long id) {
        Privilege privilege = privilegeService.getOne(id);
        privilegeService.delete(privilege);
        ;
        return true;
    }


    @PutMapping("/privilege")
    public Privilege UpdatePrivilege(@RequestBody Privilege privilege) {
        return privilegeService.save(privilege);
    }

    @PostMapping("/privilege")
    @Transactional
    public Privilege CreatePrivilege(@RequestBody Privilege privilege) {
        return privilegeService.save(privilege);
    }
}
