package com.grokonez.jwtauthentication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grokonez.jwtauthentication.model.appointment.Appointmentt;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Periodessai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long periodessaiID;
    private Date startDate;
    private String duree = "3 mois";
    private String etat ="En cours";

    @OneToOne
    private Collaborateur collaborateur;

    @OneToMany(mappedBy = "periodessai", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Appointmentt> appointments = new HashSet<>();

    public Periodessai() {
    }

    public Periodessai(Date startDate, Collaborateur collaborateur) {
        this.startDate = startDate;
        this.collaborateur = collaborateur;
    }

    public Periodessai(Date startDate, String etat, Collaborateur collaborateur) {
        this.startDate = startDate;
        this.etat = etat;
        this.collaborateur = collaborateur;
    }


    public Periodessai(Date startDate, String etat, Collaborateur collaborateur, Set<Appointmentt> appointments) {
        this.startDate = startDate;
        this.etat = etat;
        this.collaborateur = collaborateur;
        this.appointments = appointments;
    }

    public Long getPeriodessaiID() {
        return periodessaiID;
    }

    public void setPeriodessaiID(Long periodessaiID) {
        this.periodessaiID = periodessaiID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Collaborateur getCollaborateur() {
        return collaborateur;
    }

    public void setCollaborateur(Collaborateur collaborateur) {
        this.collaborateur = collaborateur;
    }


    public Set<Appointmentt> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointmentt> appointments) {
        this.appointments = appointments;
    }

    public void setAppointment(Appointmentt appointment) {
        this.appointments.add(appointment);
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }
}
