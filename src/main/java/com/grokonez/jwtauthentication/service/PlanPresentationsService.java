package com.grokonez.jwtauthentication.service;

import com.grokonez.jwtauthentication.model.Presentations;
import com.grokonez.jwtauthentication.model.appointment.Appointmentt;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PlanPresentationsService {
    Optional<Presentations> findById(Long id);

    Collection<Presentations> findAll();

    void deleteById(Long id);

    void delete(Presentations presentations);

    Presentations save(Presentations presentations);

    List<Appointmentt> getPresentationOfMail(String mail);

    Presentations getPresentationByMail(String mail);

    Presentations getOne(Long id);


}
