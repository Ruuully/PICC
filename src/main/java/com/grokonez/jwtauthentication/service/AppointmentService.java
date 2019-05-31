package com.grokonez.jwtauthentication.service;

import com.grokonez.jwtauthentication.model.appointment.Appointmentt;
import org.springframework.data.domain.Sort;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface AppointmentService {

    Optional<Appointmentt> findById(Long id);

    Collection<Appointmentt> findAll();

    void deleteById(Long id);

    void delete(Appointmentt appointmentt);

    Appointmentt save(Appointmentt appointmentt);

    Appointmentt findAppointmenttsByCode(String code);

    Appointmentt getOne(Long id);

    List<Appointmentt> getAppsOrderDate(Sort sort);

    List<Appointmentt> getAppscr(Sort sort);




}
