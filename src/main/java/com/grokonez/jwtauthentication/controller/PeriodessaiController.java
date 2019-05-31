package com.grokonez.jwtauthentication.controller;

import Exceptions.Exception.ServerException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grokonez.jwtauthentication.model.Collaborateur;
import com.grokonez.jwtauthentication.model.Periodessai;
import com.grokonez.jwtauthentication.model.appointment.Appointmentt;
import com.grokonez.jwtauthentication.service.AppointmentService;
import com.grokonez.jwtauthentication.service.CollaborateurService;
import com.grokonez.jwtauthentication.service.ParametreService;
import com.grokonez.jwtauthentication.service.PeriodessaiService;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.availability.AvailabilityData;
import microsoft.exchange.webservices.data.core.enumeration.availability.SuggestionQuality;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
import microsoft.exchange.webservices.data.core.enumeration.service.SendInvitationsOrCancellationsMode;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.misc.availability.AttendeeInfo;
import microsoft.exchange.webservices.data.misc.availability.AvailabilityOptions;
import microsoft.exchange.webservices.data.misc.availability.GetUserAvailabilityResults;
import microsoft.exchange.webservices.data.misc.availability.TimeWindow;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.availability.TimeSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/apipe")
@PreAuthorize("hasAnyAuthority('GERER_PERIODESSAI')")
public class PeriodessaiController {

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


    @GetMapping("/periodessais")
    public Collection<Periodessai> getPeriodessais() {
        return periodessaiService.findAll();

    }

    @GetMapping("/apps/{mail}")
    public Collection<Appointmentt> getAppsPE(@PathVariable String mail) {
        return periodessaiService.getPresentationsByMail(mail, Sort.by("ordreAppPeriodE").ascending());

    }


    @GetMapping("/periodessai/{id}")
    public Object getPeriodessai(@PathVariable Long id) {
        return periodessaiService.findById(id);
    }

    @DeleteMapping("/periodessai/{id}")
    public boolean deletePeriodessaiById(@PathVariable Long id) {
        Periodessai periodessai = periodessaiService.getOne(id);
        periodessaiService.delete(periodessai);
        ;
        return true;
    }


    @PutMapping("/periodessai")

    public Periodessai UpdatePeriodessai(@RequestBody Periodessai periodessai) {
        return periodessaiService.save(periodessai);
    }

    @PostMapping(value = "/periodessai")
    @JsonIgnoreProperties({"metier", "manager"})
    @Transactional
    public boolean CreatePeriodessai(@RequestBody Periodessai periodessai) throws Exception {

        periodessaiService.save(periodessai);


        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));
        String title = par.getParametreByName("Titre des rendez-vous de la période d'essai").getParametreValue();
        String body = par.getParametreByName("Message des rendez-vous de la période d'essai").getParametreValue();
        int duree = Integer.parseInt(par.getParametreByName("Durée des rendez-vous de la période d'essai").getParametreValue());
        int maxsug = Integer.parseInt(par.getParametreByName("Nombre maximum des suggestions par jour")
                .getParametreValue());
        int i;
        if (collaborateurService.findRrh() == null) {
            throw new ServerException("Responsable ressources humaines introuvable!");
        }

        for (i = 1; i < 4; i++) {
            List<AttendeeInfo> attendees = new ArrayList<>();
            attendees.add(new AttendeeInfo(periodessai.getCollaborateur().getOutlookMail()));
            attendees.add(new AttendeeInfo(collaborateurService.findRrh().getOutlookMail()));
            attendees.add(new AttendeeInfo(appOutlookMail));
            if (periodessai.getCollaborateur().getManager().isPresent()) {
                if (i == 3) {
                    attendees.add(new AttendeeInfo(periodessai.getCollaborateur().getManager().get().getOutlookMail()));

                }
            }

            AvailabilityOptions myOptions = new AvailabilityOptions();
            myOptions.setMeetingDuration(duree);
            myOptions.setMaximumNonWorkHoursSuggestionsPerDay(0);
            myOptions.setMinimumSuggestionQuality(SuggestionQuality.Excellent);
            myOptions.setMaximumSuggestionsPerDay(maxsug);
            Date interviewDate = new Date();
            Date endValue = new Date();


            if (i == 3) {
                Calendar c = Calendar.getInstance();
                c.setTime(periodessai.getStartDate());
                c.add(Calendar.DATE, (i * 30) - 10); //
                interviewDate = c.getTime();

                Calendar cS = Calendar.getInstance();
                cS.setTime(interviewDate);
                cS.add(Calendar.DATE, 4); //
                endValue = cS.getTime();
            } else {
                Calendar c = Calendar.getInstance();
                c.setTime(periodessai.getStartDate());
                c.add(Calendar.DATE, i * 30); //
                interviewDate = c.getTime();

                Calendar cS = Calendar.getInstance();
                cS.setTime(interviewDate);
                cS.add(Calendar.DATE, 3); //
                endValue = cS.getTime();
            }

            myOptions.setDetailedSuggestionsWindow(new TimeWindow(interviewDate, endValue));
            GetUserAvailabilityResults results = service.getUserAvailability(attendees,
                    new TimeWindow(interviewDate, endValue),
                    AvailabilityData.FreeBusyAndSuggestions, myOptions);
            List<TimeSuggestion> tts = new ArrayList<>();
            results.getSuggestions().forEach(suggestion -> {
                suggestion.getTimeSuggestions().forEach(timeSuggestion -> {
                            Instant current = timeSuggestion.getMeetingTime().toInstant();
                            LocalDateTime ldt = LocalDateTime.ofInstant(current, ZoneId.systemDefault());

                            if (ldt.getHour() != 12 && ldt.getHour() != 13 && ldt.getHour() != 8) {
                                tts.add(timeSuggestion);
                            }
                        }
                );
            });
            Date presentationStart = tts.iterator().next().getMeetingTime();
            Calendar c = Calendar.getInstance();
            c.setTime(presentationStart);
            c.add(Calendar.MINUTE, duree);
            Date presentationEnd;
            presentationEnd = c.getTime();

            Appointment appointment = new Appointment(service);
            appointment.setSubject(title);
            appointment.setBody(MessageBody.getMessageBodyFromText(body));
            appointment.setStart(presentationStart);
            appointment.setEnd(presentationEnd);
            appointment.getRequiredAttendees().add(periodessai.getCollaborateur().getOutlookMail());
            appointment.getRequiredAttendees().add(collaborateurService.findRrh().getOutlookMail());
            appointment.getOptionalAttendees().add(appOutlookMail);
            if (periodessai.getCollaborateur().getManager().isPresent()) {
                if (i == 3) {
                    appointment.getRequiredAttendees().add(periodessai.getCollaborateur().getManager().get().getOutlookMail());
                }
            }

            appointment.save();


            List<Collaborateur> collsOfAppointment = new ArrayList<>();
            collsOfAppointment.add(periodessai.getCollaborateur());
            collsOfAppointment.add(collaborateurService.findRrh());
            if (periodessai.getCollaborateur().getManager().isPresent()) {

                if (i == 3) {
                    collsOfAppointment.add(periodessai.getCollaborateur().getManager().get());
                }
            }
            Appointmentt newapp = new Appointmentt(presentationStart, presentationEnd, appointment.getId().toString(),
                    collsOfAppointment, periodessai, i);
            app.save(newapp);
            periodessai.setAppointment(newapp);

        }
        if (periodessai.getCollaborateur().getManager().isPresent()) {

            if (periodessai.getCollaborateur().getManager() == null) {
                throw new ServerException("Le nouveau collaborateur n'a pas de responsable!");
            }
        }


        periodessaiService.save(periodessai);


        return true;
    }


    @PostMapping(value = "/prolonger")
    @Transactional
    public boolean ProlongerPeriodessai(@RequestBody Periodessai periodessai) throws Exception {
        periodessai.setDuree("6 mois");
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));
        String title = par.getParametreByName("Titre des rendez-vous de la période d'essai").getParametreValue();
        String body = par.getParametreByName("Message des rendez-vous de la période d'essai").getParametreValue();
        int duree = Integer.parseInt(par.getParametreByName("Durée des rendez-vous de la période d'essai").getParametreValue());
        int maxsug = Integer.parseInt(par.getParametreByName("Nombre maximum des suggestions par jour")
                .getParametreValue());
        int i;
        for (i = 1; i < 4; i++) {
            List<AttendeeInfo> attendees = new ArrayList<>();
            attendees.add(new AttendeeInfo(periodessai.getCollaborateur().getOutlookMail()));
            attendees.add(new AttendeeInfo(collaborateurService.findRrh().getOutlookMail()));
            attendees.add(new AttendeeInfo(appOutlookMail));
            if (periodessai.getCollaborateur().getManager().isPresent()) {
                attendees.add(new AttendeeInfo(periodessai.getCollaborateur().getManager().get().getOutlookMail()));
            }
            AvailabilityOptions myOptions = new AvailabilityOptions();
            myOptions.setMeetingDuration(duree);
            myOptions.setMaximumNonWorkHoursSuggestionsPerDay(0);
            myOptions.setMinimumSuggestionQuality(SuggestionQuality.Excellent);
            myOptions.setMaximumSuggestionsPerDay(maxsug);
            Date interviewDate = new Date();
            Date endValue = new Date();
            Calendar c = Calendar.getInstance();

            if (i == 3) {
                c.setTime(periodessai.getStartDate());
                c.add(Calendar.DATE, 90 + (i * 30) - 10); //
                interviewDate = c.getTime();

                Calendar cS = Calendar.getInstance();
                cS.setTime(interviewDate);
                cS.add(Calendar.DATE, 4); //
                endValue = cS.getTime();
            } else {
                c.setTime(periodessai.getStartDate());
                c.add(Calendar.DATE, 90 + i * 30); //
                interviewDate = c.getTime();

                Calendar cS = Calendar.getInstance();
                cS.setTime(interviewDate);
                cS.add(Calendar.DATE, 3); //
                endValue = cS.getTime();
            }

            myOptions.setDetailedSuggestionsWindow(new TimeWindow(interviewDate, endValue));
            GetUserAvailabilityResults results = service.getUserAvailability(attendees, new TimeWindow(interviewDate
                            , endValue),
                    AvailabilityData.FreeBusyAndSuggestions, myOptions);
            List<TimeSuggestion> tts = new ArrayList<>();
            results.getSuggestions().forEach(suggestion -> {
                suggestion.getTimeSuggestions().forEach(timeSuggestion -> {
                            Instant current = timeSuggestion.getMeetingTime().toInstant();
                            LocalDateTime ldt = LocalDateTime.ofInstant(current, ZoneId.systemDefault());

                            if (ldt.getHour() != 12 && ldt.getHour() != 13 && ldt.getHour() != 8) {
                                tts.add(timeSuggestion);
                            }
                        }
                );
            });
            Date presentationStart = tts.iterator().next().getMeetingTime();
            c.setTime(presentationStart);
            c.add(Calendar.MINUTE, duree);
            Date presentationEnd;
            presentationEnd = c.getTime();

            Appointment appointment = new Appointment(service);
            appointment.setSubject(title);
            appointment.setBody(MessageBody.getMessageBodyFromText(body));
            appointment.setStart(presentationStart);
            appointment.setEnd(presentationEnd);
            appointment.getRequiredAttendees().add(periodessai.getCollaborateur().getOutlookMail());
            appointment.getRequiredAttendees().add(collaborateurService.findRrh().getOutlookMail());
            appointment.getOptionalAttendees().add(appOutlookMail);
            if (periodessai.getCollaborateur().getManager().isPresent()) {
                appointment.getRequiredAttendees().add(periodessai.getCollaborateur().getManager().get().getOutlookMail());
            }


            appointment.save();


            List<Collaborateur> collsOfAppointment = new ArrayList<>();
            collsOfAppointment.add(periodessai.getCollaborateur());
            collsOfAppointment.add(collaborateurService.findRrh());
            if (periodessai.getCollaborateur().getManager().isPresent()) {
                collsOfAppointment.add(periodessai.getCollaborateur().getManager().get());
            }
            Appointmentt newapp = new Appointmentt(presentationStart, presentationEnd, appointment.getId().toString(),
                    collsOfAppointment, periodessai, i + 3);
            app.save(newapp);
            periodessai.setAppointment(newapp);

        }


        periodessaiService.save(periodessai);
        return true;
    }


    @RequestMapping("/appointmentreport/{nbDays}")
    public Appointmentt appointmentReportByDays(@RequestBody Appointmentt appointmenttt, @PathVariable int nbDays)
            throws Exception {

        List<TimeSuggestion> tts = new ArrayList<>();
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));
        String title = par.getParametreByName("Titre des rendez-vous de la période d'essai").getParametreValue();
        String body = par.getParametreByName("Message des rendez-vous de la période d'essai").getParametreValue();
        int duree = Integer.parseInt(par.getParametreByName("Durée des rendez-vous de la période d'essai").getParametreValue());
        int maxsug = Integer.parseInt(par.getParametreByName("Nombre maximum des suggestions par jour")
                .getParametreValue());

        Appointment appoi = Appointment.bind(service, new ItemId(appointmenttt.getCodeAppointment()));

        List<Collaborateur> collsOfAppointment = appointmenttt.getCollaborateurs();

        List<AttendeeInfo> attendees = new ArrayList<>();
        for (Collaborateur collaborateur : collsOfAppointment) {
            attendees.add(new AttendeeInfo(collaborateur.getOutlookMail()));
        }


        Calendar c = Calendar.getInstance();
        c.setTime(appointmenttt.getEndTime());
        c.add(Calendar.DATE, 30); // 30 maximum delai des meetings
        Date endValue;
        endValue = c.getTime();

        c.setTime(appointmenttt.getEndTime());
        c.add(Calendar.DATE, nbDays); // 30 maximum delai des meetings
        Date sd;
        sd = c.getTime();

        AvailabilityOptions myOptions = new AvailabilityOptions();
        myOptions.setMeetingDuration(duree);
        myOptions.setMaximumNonWorkHoursSuggestionsPerDay(0);
        myOptions.setMinimumSuggestionQuality(SuggestionQuality.Excellent);
        myOptions.setMaximumSuggestionsPerDay(maxsug);

        GetUserAvailabilityResults results = service.getUserAvailability(attendees,
                new TimeWindow(sd, endValue), AvailabilityData.FreeBusyAndSuggestions, myOptions);

        results.getSuggestions().forEach(suggestion -> {
            suggestion.getTimeSuggestions().forEach(
                    timeSuggestion -> {
                        Instant current = timeSuggestion.getMeetingTime().toInstant();
                        LocalDateTime ldt = LocalDateTime.ofInstant(current, ZoneId.systemDefault());

                        if (timeSuggestion.getMeetingTime().after(sd) && ldt.getHour() != 12 && ldt.getHour() != 13 &&
                                ldt.getHour() != 8) {
                            tts.add(timeSuggestion);
                        }
                    }
            );
        });


        System.out.println(results.getSuggestions().iterator().next().getTimeSuggestions().iterator().next()
                .getMeetingTime());
        Date presentationStart = tts.iterator().next().getMeetingTime();
        // Date presentationStart = results.getSuggestions().iterator().next().getTimeSuggestions()
        // .iterator().next().getMeetingTime();
        //  Date presentationStart = dates.iterator().next();
        c.setTime(presentationStart);
        c.add(Calendar.MINUTE, duree); // 30 maximum delai des meetings
        Date presentationEnd;
        presentationEnd = c.getTime();
        System.out.println(presentationStart);
        System.out.println(presentationEnd);

        appoi.setStart(presentationStart);
        appoi.setEnd(presentationEnd);
        appoi.update(ConflictResolutionMode.AlwaysOverwrite, SendInvitationsOrCancellationsMode.SendToAllAndSaveCopy);
//heeere heere heere heere
        Appointmentt newapp = new Appointmentt(presentationStart, presentationEnd, appoi.getId().toString()
                , collsOfAppointment,
                appointmenttt.getPeriodessai(),
                appointmenttt.getOrdreAppPeriodE());
        appointmenttt.setReponse("Reporté");
        appointmenttt.setCodeAppointment("");

        app.save(appointmenttt);

        app.save(newapp);


        return newapp;
    }


    @RequestMapping("/appointmentreport")
    public Appointmentt appointmentReportt(@RequestBody Appointmentt appointmenttt) throws Exception {

        List<TimeSuggestion> tts = new ArrayList<>();
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));
        String title = par.getParametreByName("Titre des rendez-vous de la période d'essai").getParametreValue();
        String body = par.getParametreByName("Message des rendez-vous de la période d'essai").getParametreValue();
        int duree = Integer.parseInt(par.getParametreByName("Durée des rendez-vous de la période d'essai").getParametreValue());
        int maxsug = Integer.parseInt(par.getParametreByName("Nombre maximum des suggestions par jour")
                .getParametreValue());
        Periodessai pe = appointmenttt.getPeriodessai();
        System.out.println(pe.getCollaborateur().getOutlookMail());

        Appointment appoi = Appointment.bind(service, new ItemId(appointmenttt.getCodeAppointment()));

        List<Collaborateur> collsOfAppointment = appointmenttt.getCollaborateurs();


        Calendar c = Calendar.getInstance();
        c.setTime(appointmenttt.getEndTime());
        c.add(Calendar.DATE, 30);
        Date endValue;
        endValue = c.getTime();


        List<AttendeeInfo> attendees = new ArrayList<>();
        collsOfAppointment.forEach(collaborateur -> attendees.add(new AttendeeInfo(collaborateur.getOutlookMail())));


        AvailabilityOptions myOptions = new AvailabilityOptions();
        myOptions.setMeetingDuration(duree);
        myOptions.setMaximumNonWorkHoursSuggestionsPerDay(0);
        myOptions.setMinimumSuggestionQuality(SuggestionQuality.Excellent);
        myOptions.setMaximumSuggestionsPerDay(maxsug);


        GetUserAvailabilityResults results = service.getUserAvailability(attendees,
                new TimeWindow(appointmenttt.getEndTime(), endValue),
                AvailabilityData.FreeBusyAndSuggestions, myOptions);

        results.getSuggestions().forEach(suggestion -> {
            suggestion.getTimeSuggestions().forEach(timeSuggestion -> {
                        Instant current = timeSuggestion.getMeetingTime().toInstant();
                        LocalDateTime ldt = LocalDateTime.ofInstant(current, ZoneId.systemDefault());

                        if (timeSuggestion.getMeetingTime().after(appointmenttt.getEndTime())
                                && ldt.getHour() != 12 && ldt.getHour() != 13 && ldt.getHour() != 8) {
                            tts.add(timeSuggestion);
                        }
                    }
            );
        });


        System.out.println(results.getSuggestions().iterator().next().getTimeSuggestions().iterator().next()
                .getMeetingTime());
        Date presentationStart = tts.iterator().next().getMeetingTime();
        // Date presentationStart = results.getSuggestions().iterator().next().getTimeSuggestions()
        // .iterator().next().getMeetingTime();
        //  Date presentationStart = dates.iterator().next();
        c.setTime(presentationStart);
        c.add(Calendar.MINUTE, duree); // 30 maximum delai des meetings
        Date presentationEnd;
        presentationEnd = c.getTime();
        System.out.println(presentationStart);
        System.out.println(presentationEnd);

        appoi.setStart(presentationStart);
        appoi.setEnd(presentationEnd);
        appoi.update(ConflictResolutionMode.AlwaysOverwrite, SendInvitationsOrCancellationsMode.SendToAllAndSaveCopy);
        appointmenttt.setReponse("Reporté");
        appointmenttt.setCodeAppointment(null);
        app.save(appointmenttt);

        Appointmentt newapp = new Appointmentt(presentationStart, presentationEnd, appoi.getId().toString()
                , collsOfAppointment,
                appointmenttt.getPeriodessai(), appointmenttt.getOrdreAppPeriodE());

        app.save(newapp);
        pe.setAppointment(newapp);
        periodessaiService.save(pe);

        return newapp;
    }

    @PostMapping("/periodessai/rupture")
    public boolean rupturerPeriodessai(@RequestBody Periodessai periodessai) throws Exception {
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));

        Periodessai periodessaiToSave = periodessaiService.findById(periodessai.getPeriodessaiID()).get();
        periodessaiToSave.setEtat("Rupture");
        periodessaiService.save(periodessaiToSave);
        periodessaiToSave.getAppointments()
                .forEach(appointment -> {
                    Instant current = appointment.getStartTime().toInstant();
                    LocalDateTime ldt = LocalDateTime.ofInstant(current, ZoneId.systemDefault());
                    if (ldt.isAfter(LocalDateTime.now())) {
                        try {
                            Appointment appointmentOutlook = Appointment.bind(service, new ItemId(appointment.getCodeAppointment()));
                            appointmentOutlook.cancelMeeting();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        appointment.setReponse("Annulé");
                        app.save(appointment);

                    }
                });

        return true;
    }

    @PostMapping("/periodessai/valider")
    public boolean validerPeriodessai(@RequestBody Periodessai periodessai) throws Exception {
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));

        Periodessai periodessaiToSave = periodessaiService.findById(periodessai.getPeriodessaiID()).get();
        periodessaiToSave.setEtat("Validé");
        Optional<Collaborateur> collaborateurNotNew = collaborateurService.findById(periodessaiToSave.getCollaborateur().getCollaborateurId());
        collaborateurNotNew.get().setNewCol(false);
        collaborateurService.update(collaborateurNotNew.get());
        periodessaiService.save(periodessaiToSave);
        periodessaiToSave.getAppointments()
                .forEach(appointment -> {
                    Instant current = appointment.getStartTime().toInstant();
                    LocalDateTime ldt = LocalDateTime.ofInstant(current, ZoneId.systemDefault());
                    if (ldt.isAfter(LocalDateTime.now())) {
                        try {
                            Appointment appointmentOutlook = Appointment.bind(service, new ItemId(appointment.getCodeAppointment()));
                            appointmentOutlook.cancelMeeting();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        appointment.setReponse("Annulé");
                        app.save(appointment);

                    }
                });

        return true;
    }
}



