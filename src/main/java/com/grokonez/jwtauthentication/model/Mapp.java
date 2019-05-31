package com.grokonez.jwtauthentication.model;

import microsoft.exchange.webservices.data.property.complex.availability.TimeSuggestion;

import java.util.List;

public class Mapp {
    private String nv;
    private List<TimeSuggestion> ts;

    public Mapp(String nv, List<TimeSuggestion> ts) {
        this.nv = nv;
        this.ts = ts;
    }

    public Mapp() {
    }

    public String getNv() {
        return nv;
    }

    public void setNv(String nv) {
        this.nv = nv;
    }

    public List<TimeSuggestion> getTs() {
        return ts;
    }

    public void setTs(List<TimeSuggestion> ts) {
        this.ts = ts;
    }
}
