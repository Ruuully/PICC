package com.grokonez.jwtauthentication.service;

import com.grokonez.jwtauthentication.model.Privilege;

import java.util.Collection;
import java.util.Optional;

public interface PrivilegeService {
    Optional<Privilege> findById(Long id);

    Collection<Privilege> findAll();

    void deleteById(Long id);

    void delete(Privilege privilege);

    Privilege save(Privilege privilege);

    Privilege getOne(Long id);

}
