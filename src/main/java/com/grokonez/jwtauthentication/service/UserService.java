package com.grokonez.jwtauthentication.service;

import com.grokonez.jwtauthentication.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long id);

    Collection<User> findAll();

    void deleteById(Long id);

    void delete(User user);

    User save(User user);

    User getOne(Long id);

}
