package com.grokonez.jwtauthentication.service;

import com.grokonez.jwtauthentication.model.Role;

import java.util.Collection;
import java.util.Optional;

public interface RoleService {
    Optional<Role> findById(Long id);

    Collection<Role> findAll();

    void deleteById(Long id);

    void delete(Role role);

    Role save(Role role);

    Role getOne(Long id);
}
