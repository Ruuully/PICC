package com.grokonez.jwtauthentication.DAO;

import com.grokonez.jwtauthentication.model.appointment.Appointmentt;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmenttDAO extends JpaRepository<Appointmentt, Long> {
    /*  @Query("SELECT a FROM Appointmentt a WHERE a.presentations = ?1 ")
      List<Appointmentt> findAppointmenttsByMail(String outlookMail); */
    @Query("SELECT a FROM Appointmentt a WHERE a.codeAppointment = ?1 ")
    Appointmentt findAppointmenttsByCode(String code);


    @Query("SELECT a FROM Appointmentt a")
    List<Appointmentt> getAppsOrderDate(Sort sort);

    @Query("SELECT a FROM Appointmentt a WHERE a.reponse='annulé' or a.reponse = 'reporté' ")
    List<Appointmentt> getAppscr(Sort sort);


}
