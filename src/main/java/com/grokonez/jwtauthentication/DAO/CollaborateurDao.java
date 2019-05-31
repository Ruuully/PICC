package com.grokonez.jwtauthentication.DAO;

import com.grokonez.jwtauthentication.model.Collaborateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface CollaborateurDao extends JpaRepository<Collaborateur, Long> {
    @Query("SELECT c FROM Collaborateur c WHERE c.metier.name = 'Manager'")
    List<Collaborateur> findAllManagers();

    @Query("SELECT c FROM Collaborateur c WHERE c.outlookMail = ?1 ")
    Optional<Collaborateur> findbyEmail(String outlookMail);

    @Query("SELECT c FROM Collaborateur c WHERE c.newCol = true ")
    List<Collaborateur> findNewColls();

    @Query("SELECT c FROM Collaborateur c WHERE c.metier.name = 'Rrh'")
    Collaborateur findRrh();
}
