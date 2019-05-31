package com.grokonez.jwtauthentication.service;

import com.grokonez.jwtauthentication.model.Metier;

import java.util.Collection;
import java.util.Optional;


public interface MetierService {
    Optional<Metier> findById(Long id);

    Collection<Metier> findAll();

    void deleteById(Long id);

    void delete(Metier metier);

    Metier save(Metier metier);

    Metier getOne(Long id);

}
