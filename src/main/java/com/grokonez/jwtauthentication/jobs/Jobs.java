package com.grokonez.jwtauthentication.jobs;

import com.grokonez.jwtauthentication.model.Periodessai;
import com.grokonez.jwtauthentication.model.appointment.Appointmentt;
import com.grokonez.jwtauthentication.service.AppointmentService;
import com.grokonez.jwtauthentication.service.CollaborateurService;
import com.grokonez.jwtauthentication.service.ParametreService;
import com.grokonez.jwtauthentication.service.PeriodessaiService;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.MeetingResponseType;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceResponseException;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.Attendee;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.property.complex.availability.CalendarEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("jobs")

public class Jobs {

    @Value("${spring.outlook.mail}")
    private String appOutlookMail;

    @Value("${spring.outlook.password}")
    private String appOutlookPassword;


    @Autowired
    private PeriodessaiService periodessaiService;

    @Autowired
    private ParametreService par;

    @Autowired
    private CollaborateurService collaborateurService;
    @Autowired
    private AppointmentService app;



}
