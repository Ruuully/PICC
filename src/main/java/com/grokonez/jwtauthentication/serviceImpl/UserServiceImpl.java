package com.grokonez.jwtauthentication.serviceImpl;

import com.grokonez.jwtauthentication.DAO.MetierDAO;
import com.grokonez.jwtauthentication.DAO.UserDAO;
import com.grokonez.jwtauthentication.model.User;
import com.grokonez.jwtauthentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
@Service

public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public Optional<User> findById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    public Collection<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public void deleteById(Long id) {
        userDAO.deleteById(id);
    }

    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }

    @Override
    public User save(User user) {
        return userDAO.save(user);
    }

    @Override
    public User getOne(Long id) {
        return userDAO.getOne(id);
    }
}
