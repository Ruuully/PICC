package com.grokonez.jwtauthentication.model.appointment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grokonez.jwtauthentication.model.Collaborateur;
import com.grokonez.jwtauthentication.model.Periodessai;
import com.grokonez.jwtauthentication.model.Presentations;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Appointmentt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAppointment;
    private Date startTime;
    private Date endTime;
    @Column(unique = true)
    private String codeAppointment;
    private String reponse ="Actif";

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "appointment_collaborateur",
            joinColumns = @JoinColumn(name = "idAppointment"),
            inverseJoinColumns = @JoinColumn(name = "collaborateurId"))
    private List<Collaborateur> collaborateurs;

    @ManyToOne
    @JoinColumn(name = "periodessai")
    private Periodessai periodessai;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "present")
    private Presentations presentations;

    private int ordreAppPeriodE;

    public Appointmentt() {
    }

    public Appointmentt(Date startTime, Date endTime, String codeAppointment, List<Collaborateur> collaborateurs) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.codeAppointment = codeAppointment;
        this.collaborateurs = collaborateurs;
    }

    public Appointmentt(Date startTime, Date endTime, String codeAppointment, List<Collaborateur> collaborateurs, Periodessai periodessai) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.codeAppointment = codeAppointment;
        this.collaborateurs = collaborateurs;
        this.periodessai = periodessai;
    }

    public Appointmentt(Date startTime, Date endTime, String codeAppointment, List<Collaborateur> collaborateurs, Periodessai periodessai, int ordreAppPeriodE) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.codeAppointment = codeAppointment;
        this.collaborateurs = collaborateurs;
        this.periodessai = periodessai;
        this.ordreAppPeriodE = ordreAppPeriodE;
    }

    public Appointmentt(Date startTime, Date endTime, String codeAppointment, List<Collaborateur> collaborateurs, Presentations presentations) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.codeAppointment = codeAppointment;
        this.collaborateurs = collaborateurs;
        this.presentations = presentations;
    }

    public int getOrdreAppPeriodE() {
        return ordreAppPeriodE;
    }

    public void setOrdreAppPeriodE(int ordreAppPeriodE) {
        this.ordreAppPeriodE = ordreAppPeriodE;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public Long getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(Long idAppointment) {
        this.idAppointment = idAppointment;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCodeAppointment() {
        return codeAppointment;
    }

    public void setCodeAppointment(String codeAppointment) {
        this.codeAppointment = codeAppointment;
    }

    public Periodessai getPeriodessai() {
        return periodessai;
    }

    public void setPeriodessai(Periodessai periodessai) {
        this.periodessai = periodessai;
    }

    public List<Collaborateur> getCollaborateurs() {
        return collaborateurs;
    }

    public void setCollaborateurs(List<Collaborateur> collaborateurs) {
        this.collaborateurs = collaborateurs;
    }

    public Presentations getPresentations() {
        return presentations;
    }

    public void setPresentations(Presentations presentations) {
        this.presentations = presentations;
    }
}
