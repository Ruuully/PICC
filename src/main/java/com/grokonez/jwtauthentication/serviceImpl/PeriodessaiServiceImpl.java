package com.grokonez.jwtauthentication.serviceImpl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grokonez.jwtauthentication.DAO.PeriodessaiDAO;
import com.grokonez.jwtauthentication.model.Periodessai;
import com.grokonez.jwtauthentication.model.appointment.Appointmentt;
import com.grokonez.jwtauthentication.service.PeriodessaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service

public class PeriodessaiServiceImpl implements PeriodessaiService {
    @Autowired
    private PeriodessaiDAO periodessaiDAO;

    @Override
    public Optional<Periodessai> findById(Long id) {
        return periodessaiDAO.findById(id);
    }

    @Override
    public Collection<Periodessai> findAll() {
        return periodessaiDAO.findAll();
    }

    @Override
    public void deleteById(Long id) {
        periodessaiDAO.deleteById(id);
    }

    @Override
    public void delete(Periodessai periodessai) {
        periodessaiDAO.delete(periodessai);
    }

    @Override
    public Periodessai save(Periodessai periodessai) {
        return periodessaiDAO.save(periodessai);
    }

    @Override
    public Periodessai getOne(Long id) {
        return periodessaiDAO.getOne(id);
    }

    @Override
    public List<Appointmentt> getPresentationsByMail(String mail, Sort sort) {
        List<Appointmentt> apps =  periodessaiDAO.getPresentationOfMail(mail,Sort.by("ordreAppPeriodE").ascending());
return apps;
    }


}
