package com.grokonez.jwtauthentication.serviceImpl;

import com.grokonez.jwtauthentication.DAO.PrivilegeDAO;
import com.grokonez.jwtauthentication.model.Privilege;
import com.grokonez.jwtauthentication.model.Privilege;
import com.grokonez.jwtauthentication.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
@Service

public class PrivilegeServiceImpl implements PrivilegeService {
    @Autowired
    private PrivilegeDAO roleDAO;

    @Override
    public Optional<Privilege> findById(Long id) {
        return roleDAO.findById(id);
    }

    @Override
    public Collection<Privilege> findAll() {
        return roleDAO.findAll();
    }

    @Override
    public void deleteById(Long id) {
        roleDAO.deleteById(id);
    }

    @Override
    public void delete(Privilege privilege) {
        roleDAO.delete(privilege);
    }

    @Override
    public Privilege save(Privilege privilege) {
        return roleDAO.save(privilege);
    }

    @Override
    public Privilege getOne(Long id) {
        return roleDAO.getOne(id);
    }
}

