package com.grokonez.jwtauthentication.model;

import javax.persistence.*;

@Entity
public class Parametre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parametreId;
    @Column(unique = true)
    private String parametreName;
    private String parametreType;
    private String parametreValue;

    public Parametre() {
    }

    public Parametre(String parametreName, String parametreValue) {
        this.parametreName = parametreName;
        this.parametreValue = parametreValue;
    }

    public Parametre(String parametreName, String parametreType, String parametreValue) {
        this.parametreName = parametreName;
        this.parametreType = parametreType;
        this.parametreValue = parametreValue;
    }

    public String getParametreType() {
        return parametreType;
    }

    public void setParametreType(String parametreType) {
        this.parametreType = parametreType;
    }

    public Long getParametreId() {
        return parametreId;
    }

    public void setParametreId(Long parametreId) {
        this.parametreId = parametreId;
    }

    public String getParametreName() {
        return parametreName;
    }

    public void setParametreName(String parametreName) {
        this.parametreName = parametreName;
    }

    public String getParametreValue() {
        return parametreValue;
    }

    public void setParametreValue(String parametreValue) {
        this.parametreValue = parametreValue;
    }
}
