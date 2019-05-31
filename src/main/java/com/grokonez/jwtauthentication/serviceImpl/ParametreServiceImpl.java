package com.grokonez.jwtauthentication.serviceImpl;

import com.grokonez.jwtauthentication.DAO.ParametreDAO;
import com.grokonez.jwtauthentication.model.Parametre;
import com.grokonez.jwtauthentication.service.ParametreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ParametreServiceImpl implements ParametreService {
    @Autowired
    private ParametreDAO parametreDAO;

    @Override
    public Optional<Parametre> findById(Long id) {
        return parametreDAO.findById(id);
    }

    @Override
    public Collection<Parametre> findAll() {
        return parametreDAO.findAll();
    }

    @Override
    public void deleteById(Long id) {
        parametreDAO.deleteById(id);
    }

    @Override
    public void delete(Parametre parametre) {
        parametreDAO.delete(parametre);
    }

    @Override
    public Parametre save(Parametre parametre) {
        return parametreDAO.save(parametre);
    }

    @Override
    public Parametre getParametreByName(String name) {
        return parametreDAO.getParametreByName(name);
    }

    @Override
    public Parametre getOne(Long id) {
        return parametreDAO.getOne(id);
    }
}
