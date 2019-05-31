package com.grokonez.jwtauthentication.serviceImpl;

import Exceptions.Exception.ClientException;
import Exceptions.Exception.ServerException;
import com.grokonez.jwtauthentication.DAO.MetierDAO;
import com.grokonez.jwtauthentication.model.Collaborateur;
import com.grokonez.jwtauthentication.model.Metier;
import com.grokonez.jwtauthentication.service.MetierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service

public class MetierServiceImpl implements MetierService {

    @Autowired
    private MetierDAO metierDAO;

    @Override
    public Optional<Metier> findById(Long id) {
        return metierDAO.findById(id);
    }

    @Override
    public Collection<Metier> findAll() {
        return metierDAO.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Optional<Metier> metierToDelete = metierDAO.findById(id);
        if(metierToDelete.get().getCollaborateurs() != null){
            throw new ServerException("Impossible de supprimer, ce métier est affecté à plusieurs collaborateurs ");}

        metierDAO.deleteById(id);

    }

    @Override
    public void delete(Metier metier) {
        metierDAO.delete(metier);
    }

    @Override
    public Metier save(Metier metier) {
        if(metierDAO.findByName(metier.getName()) != null){
            throw new ClientException("Métier existe déjà");
        }
        return metierDAO.save(metier);    }

    @Override
    public Metier getOne(Long id) {
        return metierDAO.getOne(id);
    }
}
