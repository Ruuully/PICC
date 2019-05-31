package com.grokonez.jwtauthentication.service;

import com.grokonez.jwtauthentication.model.Parametre;

import java.util.Collection;
import java.util.Optional;

public interface ParametreService {
    Optional<Parametre> findById(Long id);

    Collection<Parametre> findAll();

    void deleteById(Long id);

    void delete(Parametre parametre);

    Parametre save(Parametre parametre);

    Parametre getParametreByName(String name);

    Parametre getOne(Long id);


}
