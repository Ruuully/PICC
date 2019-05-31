package com.grokonez.jwtauthentication.serviceImpl;

import com.grokonez.jwtauthentication.DAO.RoleDAO;
import com.grokonez.jwtauthentication.model.Role;
import com.grokonez.jwtauthentication.model.Role;
import com.grokonez.jwtauthentication.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
@Service

public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDAO roleDAO;

    @Override
    public Optional<Role> findById(Long id) {
        return roleDAO.findById(id);
    }

    @Override
    public Collection<Role> findAll() {
        return roleDAO.findAll();
    }

    @Override
    public void deleteById(Long id) {
        roleDAO.deleteById(id);
    }

    @Override
    public void delete(Role role) {
        roleDAO.delete(role);
    }

    @Override
    public Role save(Role role) {
        return roleDAO.save(role);
    }

    @Override
    public Role getOne(Long id) {
        return roleDAO.getOne(id);
    }

}
