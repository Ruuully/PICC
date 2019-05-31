package com.grokonez.jwtauthentication.model;


import java.util.Date;
import java.util.List;

public class AppointmentOutlook {


    private Date startTime;
    private Date endTime;
    private String title;
    private String type;
    private String status;
    private List<String> attendees;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<String> attendees) {
        this.attendees = attendees;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addAttendee(String attendee) {
        this.attendees.add(attendee);
    }
    public AppointmentOutlook() {
    }

    public AppointmentOutlook(Date startTime, Date endTime, String title) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
    }

    public AppointmentOutlook(Date startTime, Date endTime, String title, List<String> attendees) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.attendees = attendees;
    }

    public AppointmentOutlook(Date startTime, Date endTime, String title, String type, List<String> attendees) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.type = type;
        this.attendees = attendees;
    }

    public AppointmentOutlook(Date startTime, Date endTime, String title, String type, String status, List<String> attendees) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.type = type;
        this.status = status;
        this.attendees = attendees;
    }
}
