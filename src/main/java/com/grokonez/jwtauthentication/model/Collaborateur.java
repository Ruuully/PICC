package com.grokonez.jwtauthentication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grokonez.jwtauthentication.model.appointment.Appointmentt;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
public class Collaborateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long collaborateurId;

    @Column(unique = true)
    @NotBlank(message = "champ obligatoire")
    private String outlookMail;

    @NotBlank(message = "champ obligatoire")
    private String namee;

    private boolean newCol = false;

    private Date dateArrivee;

    @ManyToOne
    private Metier metier;

    @ManyToOne
    @JoinColumn(name = "responsable")
    @JsonIgnoreProperties(value = { "manager" })
    private Collaborateur manager;

    @OneToMany(mappedBy = "manager")
    @JsonIgnore
    private Set<Collaborateur> subordinates = new HashSet<>();

    @OneToOne(mappedBy = "collaborateur")
    @JsonIgnoreProperties(value = { "collaborateur" })
    private Periodessai periodessai;

    @JsonIgnore
    @OneToOne(mappedBy = "newCollaborateur")
    private Presentations presentations;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Appointmentt> appointments;


    public Collaborateur() {
    }

    public Collaborateur(String outlookMail, String namee) {
        this.outlookMail = outlookMail;
        this.namee = namee;
    }

    public Collaborateur(String outlookMail, String namee, Metier metier) {
        this.outlookMail = outlookMail;
        this.namee = namee;
        this.metier = metier;
    }

    public Collaborateur(String outlookMail, String namee, boolean newCol, Metier metier) {
        this.outlookMail = outlookMail;
        this.namee = namee;
        this.newCol = newCol;
        this.metier = metier;
    }

    public Collaborateur(String outlookMail, String namee, boolean newCol, Date dateArrivee, Metier metier) {
        this.outlookMail = outlookMail;
        this.namee = namee;
        this.newCol = newCol;
        this.dateArrivee = dateArrivee;
        this.metier = metier;
    }

    public Collaborateur(String outlookMail, String namee, boolean newCol, Date dateArrivee, Metier metier, Collaborateur manager) {
        this.outlookMail = outlookMail;
        this.namee = namee;
        this.newCol = newCol;
        this.dateArrivee = dateArrivee;
        this.metier = metier;
        this.manager = manager;
    }

    public Collaborateur(String outlookMail, String namee, boolean newCol, Date dateArrivee, Metier metier, Collaborateur manager, Set<Collaborateur> subordinates) {
        this.outlookMail = outlookMail;
        this.namee = namee;
        this.newCol = newCol;
        this.dateArrivee = dateArrivee;
        this.metier = metier;
        this.manager = manager;
        this.subordinates = subordinates;
    }

    public Collaborateur(String outlookMail, String namee, boolean newCol, Date dateArrivee, Metier metier, Collaborateur manager, Periodessai periodessai) {
        this.outlookMail = outlookMail;
        this.namee = namee;
        this.newCol = newCol;
        this.dateArrivee = dateArrivee;
        this.metier = metier;
        this.manager = manager;
        this.periodessai = periodessai;
    }

    public String getOutlookMail() {
        return outlookMail;
    }

    public void setOutlookMail(String outlookMail) {
        this.outlookMail = outlookMail;
    }

    public String getNamee() {
        return namee;
    }

    public void setNamee(String namee) {
        this.namee = namee;
    }

    public Metier getMetier() {
        return metier;
    }

    public void setMetier(Metier metier) {
        this.metier = metier;
    }

    public Long getCollaborateurId() {
        return collaborateurId;
    }

    public void setCollaborateurId(Long collaborateurId) {
        this.collaborateurId = collaborateurId;
    }

    public boolean isNewCol() {
        return newCol;
    }

    public void setNewCol(boolean newCol) {
        this.newCol = newCol;
    }

    public Date getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(Date dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public Optional<Collaborateur> getManager() {
        return Optional.ofNullable(manager);
    }

    public void setManager(Collaborateur manager) {
        this.manager = manager;
    }

    public Set<Collaborateur> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(Set<Collaborateur> subordinates) {
        this.subordinates = subordinates;
    }

    public Periodessai getPeriodessai() {
        return periodessai;
    }

    public void setPeriodessai(Periodessai periodessai) {
        this.periodessai = periodessai;
    }

    public Presentations getPresentations() {
        return presentations;
    }

    public void setPresentations(Presentations presentations) {
        this.presentations = presentations;
    }

    public List<Appointmentt> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointmentt> appointments) {
        this.appointments = appointments;
    }


}
