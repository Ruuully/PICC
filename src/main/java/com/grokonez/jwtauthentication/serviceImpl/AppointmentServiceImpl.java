package com.grokonez.jwtauthentication.serviceImpl;

import com.grokonez.jwtauthentication.DAO.AppointmenttDAO;
import com.grokonez.jwtauthentication.model.appointment.Appointmentt;
import com.grokonez.jwtauthentication.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmenttDAO appointmenttDAO;

    @Override
    public Optional<Appointmentt> findById(Long id) {
        return appointmenttDAO.findById(id);
    }

    @Override
    public Collection<Appointmentt> findAll() {
        return appointmenttDAO.findAll();
    }

    @Override
    public void deleteById(Long id) {
        appointmenttDAO.deleteById(id);
    }

    @Override
    public void delete(Appointmentt appointmentt) {
        appointmenttDAO.delete(appointmentt);
    }

    @Override
    public Appointmentt save(Appointmentt appointmentt) {
        return appointmenttDAO.save(appointmentt);
    }

    @Override
    public Appointmentt findAppointmenttsByCode(String code) {
        return appointmenttDAO.findAppointmenttsByCode(code);
    }

    @Override
    public Appointmentt getOne(Long id) {
        return appointmenttDAO.getOne(id);
    }

    @Override
    public List<Appointmentt> getAppsOrderDate(Sort sort) {
        return appointmenttDAO.getAppsOrderDate(Sort.by("startTime").ascending());
    }

    @Override
    public List<Appointmentt> getAppscr(Sort sort) {
        return appointmenttDAO.getAppscr(Sort.by("startTime").ascending());
    }
}
