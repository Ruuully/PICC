package com.grokonez.jwtauthentication.serviceImpl;

import com.grokonez.jwtauthentication.DAO.PresentationsDAO;
import com.grokonez.jwtauthentication.model.Presentations;
import com.grokonez.jwtauthentication.model.appointment.Appointmentt;
import com.grokonez.jwtauthentication.service.PlanPresentationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service

public class PlanPresentationServiceImpl implements PlanPresentationsService {
    @Autowired
    private PresentationsDAO presentationsDAO;

    @Override
    public Optional<Presentations> findById(Long id) {
        return presentationsDAO.findById(id);
    }

    @Override
    public Collection<Presentations> findAll() {
        return presentationsDAO.findAll();
    }

    @Override
    public void deleteById(Long id) {
        presentationsDAO.deleteById(id);
    }

    @Override
    public void delete(Presentations presentations) {
        presentationsDAO.delete(presentations);
    }

    @Override
    public Presentations save(Presentations presentations) {
        return presentationsDAO.save(presentations);
    }

    @Override
    public List<Appointmentt> getPresentationOfMail(String mail) {
        return presentationsDAO.getPresentationOfMail(mail);
    }

    @Override
    public Presentations getPresentationByMail(String mail) {
        return presentationsDAO.getPresentationByMail(mail);
    }

    @Override
    public Presentations getOne(Long id) {
        return presentationsDAO.getOne(id);
    }
}
