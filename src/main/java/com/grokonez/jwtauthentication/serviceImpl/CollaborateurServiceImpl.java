package com.grokonez.jwtauthentication.serviceImpl;

import Exceptions.Exception.ClientException;
import Exceptions.Exception.ServerException;
import com.grokonez.jwtauthentication.DAO.CollaborateurDao;
import com.grokonez.jwtauthentication.model.Collaborateur;
import com.grokonez.jwtauthentication.service.CollaborateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CollaborateurServiceImpl implements CollaborateurService {

    @Autowired
    private CollaborateurDao collaborateurDao;

    @Override
    public Optional<Collaborateur> findById(Long id) {
        return collaborateurDao.findById(id);
    }

    @Override
    public Collection<Collaborateur> findAll() {
        return collaborateurDao.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Optional<Collaborateur> collaborateurToDelete = collaborateurDao.findById(id);
        if(collaborateurToDelete.get().getAppointments() != null){
            throw new ServerException("Impossible de supprimer, plusieurs rendez-vous sont affectés à " + collaborateurToDelete.get().getNamee() +"/");}

        collaborateurDao.deleteById(id);
    }

    @Override
    public void delete(Collaborateur collaborateur) {
        System.out.println(collaborateur.getAppointments());
        if(!collaborateur.getAppointments().isEmpty()){
            throw new ServerException("plusieurs rendez-vous sont affectés à " + collaborateur.getNamee());}
        collaborateurDao.delete(collaborateur);
    }

    @Override
    public Collaborateur save(Collaborateur collaborateur) {
        if(collaborateur.getManager().isPresent()) {
            if (collaborateur.getManager() != null) {
                Optional<Collaborateur> colManager = collaborateurDao.findById(collaborateur.getManager().get().getCollaborateurId());


                if (colManager.get().getManager().get().getCollaborateurId() == collaborateur.getCollaborateurId()) {
                    throw new ClientException("Changez le responsable!");

                }
            }
        }
        if (collaborateurDao.findbyEmail(collaborateur.getOutlookMail()).isPresent()) {
            throw new ClientException("Addresse mail déjà utilisée!");
        }
        return collaborateurDao.save(collaborateur);
    }
    @Override
    public Collaborateur update(Collaborateur collaborateur) {
        Optional<Collaborateur> coll =collaborateurDao.findbyEmail(collaborateur.getOutlookMail());
        Optional<Collaborateur> manager = collaborateur.getManager();
if (manager.isPresent()){
        Optional<Collaborateur> colManager = collaborateurDao.findById(manager.get().getCollaborateurId());
        if( coll.isPresent() && !collaborateur.getCollaborateurId().equals(coll.get().getCollaborateurId())){
            throw new ClientException("Adresse mail déjà utilisée!");
        }
        if(colManager.isPresent()) {
            if (colManager.get().getManager().get().getCollaborateurId() == collaborateur.getCollaborateurId()) {
                throw new ClientException("Vous devez changer le responsable (" + collaborateur.getNamee() + " est le responsable de " +
                        collaborateur.getManager().get().getNamee() + ")!");

            }
            if (collaborateur.getManager().get().getCollaborateurId() == collaborateur.getCollaborateurId()) {
                throw new ClientException("Un collaborateur ne doit pas être le responsable de lui même!");

            }
        }
}
        return collaborateurDao.save(collaborateur);
    }

    /*
    @Override
    public boolean checkIfEmailExists(String email) {
        if(collaborateurDao.findbyEmail(email) != null){
            return true;
        }
        else return false;

    }
*/
    @Override
    public List<Collaborateur> findAllManagers() {
        return collaborateurDao.findAllManagers();
    }

    @Override
    public Optional<Collaborateur> findbyEmail(String outlookMail) {
        return collaborateurDao.findbyEmail(outlookMail);
    }

    @Override
    public List<Collaborateur> findNewColls() {
        return collaborateurDao.findNewColls();
    }

    @Override
    public Collaborateur getOne(Long id) {
        return collaborateurDao.getOne(id);
    }

    @Override
    public Collaborateur findRrh() {
        return collaborateurDao.findRrh();
    }
}
