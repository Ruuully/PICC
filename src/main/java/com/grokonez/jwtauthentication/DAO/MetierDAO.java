package com.grokonez.jwtauthentication.DAO;

import com.grokonez.jwtauthentication.model.Collaborateur;
import com.grokonez.jwtauthentication.model.Metier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface MetierDAO extends JpaRepository<Metier, Long> {
    @Query("SELECT m FROM Metier m WHERE m.name = ?1 ")
    Metier findByName(String name);

}
