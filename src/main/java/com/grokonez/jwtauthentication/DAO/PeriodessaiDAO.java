package com.grokonez.jwtauthentication.DAO;

import com.grokonez.jwtauthentication.model.Periodessai;
import com.grokonez.jwtauthentication.model.appointment.Appointmentt;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeriodessaiDAO extends JpaRepository<Periodessai, Long> {
    @Query("SELECT a FROM Appointmentt a WHERE a.periodessai.collaborateur.outlookMail = ?1")
    List<Appointmentt> getPresentationOfMail(String mail, Sort sort);
}
