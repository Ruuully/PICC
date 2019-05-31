package com.grokonez.jwtauthentication.service;

import com.grokonez.jwtauthentication.model.Periodessai;
import com.grokonez.jwtauthentication.model.appointment.Appointmentt;
import org.springframework.data.domain.Sort;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PeriodessaiService {
    Optional<Periodessai> findById(Long id);

    Collection<Periodessai> findAll();

    void deleteById(Long id);

    void delete(Periodessai periodessai);

    Periodessai save(Periodessai periodessai);

    Periodessai getOne(Long id);

    List<Appointmentt> getPresentationsByMail(String mail, Sort sort);


}
