package com.grokonez.jwtauthentication.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grokonez.jwtauthentication.model.appointment.Appointmentt;

import javax.persistence.*;
import java.util.List;

@Entity
public class Presentations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String etat;
    @OneToOne
    private Collaborateur newCollaborateur;

    @OneToMany(mappedBy = "presentations", fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "presentations" })
    private List<Appointmentt> appointments;


    public Presentations() {
    }

    public Presentations(String etat, Collaborateur newCollaborateur, List<Appointmentt> appointments) {
        this.etat = etat;
        this.newCollaborateur = newCollaborateur;
        this.appointments = appointments;
    }

    public Presentations(Collaborateur newCollaborateur, List<Appointmentt> appointments) {
        this.newCollaborateur = newCollaborateur;
        this.appointments = appointments;
    }

    public Presentations(Collaborateur newCollaborateur) {
        this.newCollaborateur = newCollaborateur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Collaborateur getNewCollaborateur() {
        return newCollaborateur;
    }

    public void setNewCollaborateur(Collaborateur newCollaborateur) {
        this.newCollaborateur = newCollaborateur;
    }

    public List<Appointmentt> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointmentt> appointments) {
        this.appointments = appointments;
    }

    public void setAppointment(Appointmentt appointment) {
        this.appointments.add(appointment);
    }
}
