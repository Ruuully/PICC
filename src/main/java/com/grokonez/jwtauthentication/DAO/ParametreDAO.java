package com.grokonez.jwtauthentication.DAO;

import com.grokonez.jwtauthentication.model.Parametre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametreDAO extends JpaRepository<Parametre, Long> {
    @Query("SELECT p FROM Parametre p WHERE p.parametreName=?1")
    Parametre getParametreByName(String name);
}
