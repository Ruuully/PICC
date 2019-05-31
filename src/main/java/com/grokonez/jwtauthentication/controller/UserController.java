package com.grokonez.jwtauthentication.controller;

import com.grokonez.jwtauthentication.model.User;
import com.grokonez.jwtauthentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Collection;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/apiu")
@PreAuthorize("hasAnyAuthority('GERER_UTILISATEUR')")

public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public Collection<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping(value = "/user/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Object getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @DeleteMapping("/user/{id}")
    public boolean deleteUser(@PathVariable Long id) {
        User user = userService.getOne(id);
        userService.delete(user);
        ;
        return true;
    }


    @PutMapping("/user")
    public User UpdateUser(@RequestBody User user) {
        return userService.save(user);
    }

    @PostMapping("/user")
    @Transactional
    public User CreateUser(@RequestBody User user) {
        return userService.save(user);
    }
}
