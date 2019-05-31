package com.grokonez.jwtauthentication.service;

import com.grokonez.jwtauthentication.model.Collaborateur;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CollaborateurService {
    Optional<Collaborateur> findById(Long id);

    Collection<Collaborateur> findAll();

    void deleteById(Long id);

    void delete(Collaborateur collaborateur);

    Collaborateur save(Collaborateur collaborateur);

    Collaborateur update(Collaborateur collaborateur);

    List<Collaborateur> findAllManagers();

    Optional<Collaborateur> findbyEmail(String outlookMail);
   // public boolean checkIfEmailExists(String email);

    List<Collaborateur> findNewColls();

    Collaborateur getOne(Long id);

    Collaborateur findRrh();


}
