package com.grokonez.jwtauthentication.DAO;

import com.grokonez.jwtauthentication.model.Presentations;
import com.grokonez.jwtauthentication.model.appointment.Appointmentt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface PresentationsDAO extends JpaRepository<Presentations, Long> {
    @Query("SELECT appointments FROM Presentations a WHERE a.newCollaborateur.outlookMail = ?1")
    List<Appointmentt> getPresentationOfMail(String mail);

    @Query("SELECT p FROM Presentations p WHERE p.newCollaborateur.outlookMail = ?1")
    Presentations getPresentationByMail(String mail);


}

