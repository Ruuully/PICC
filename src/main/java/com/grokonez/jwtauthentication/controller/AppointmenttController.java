package com.grokonez.jwtauthentication.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.grokonez.jwtauthentication.model.*;
import com.grokonez.jwtauthentication.model.appointment.Appointmentt;
import com.grokonez.jwtauthentication.service.*;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.availability.AvailabilityData;
import microsoft.exchange.webservices.data.core.enumeration.availability.SuggestionQuality;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.MeetingResponseType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceResponseException;
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.misc.availability.AttendeeInfo;
import microsoft.exchange.webservices.data.misc.availability.AvailabilityOptions;
import microsoft.exchange.webservices.data.misc.availability.GetUserAvailabilityResults;
import microsoft.exchange.webservices.data.misc.availability.TimeWindow;
import microsoft.exchange.webservices.data.property.complex.Attendee;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.availability.CalendarEvent;
import microsoft.exchange.webservices.data.property.complex.availability.TimeSuggestion;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/app")
public class AppointmenttController {


    @Autowired
    private AppointmentService app;
    @Autowired
    private CollaborateurService coll;
    @Autowired
    private PlanPresentationsService pre;

    @Autowired
    private ParametreService par;
    @Autowired
    private PeriodessaiService periodessaiService;

    @Value("${spring.outlook.mail}")
    private String appOutlookMail;

    @Value("${spring.outlook.password}")
    private String appOutlookPassword;




/*
    @GetMapping("appointment/{email}/{datearrivee}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")


    public boolean dispp(@PathVariable String email ,@PathVariable Date datearrivee) throws Exception {

        List<CalendarEvent> tts = new ArrayList<>() ;
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));
        List<Appointmentt> managers = coll.findAllManagers();
        for (Appointmentt manager : managers) {
            Calendar c = Calendar.getInstance();
            c.setTime(datearrivee);
            c.add(Calendar.DATE, 30); // 30 maximum delai des meetings
            Date endValue;
            endValue = c.getTime();

            List<AttendeeInfo> attendees = new ArrayList<>();
            attendees.add(new AttendeeInfo(email));
            attendees.add(new AttendeeInfo(manager.getOutlookMail()));

            AvailabilityOptions myOptions = new AvailabilityOptions();
            myOptions.setMeetingDuration(60);
            myOptions.setMaximumNonWorkHoursSuggestionsPerDay(0);
            myOptions.setMinimumSuggestionQuality(SuggestionQuality.Good);
            myOptions.setMaximumSuggestionsPerDay(5);
            myOptions.setDetailedSuggestionsWindow(new TimeWindow(datearrivee, endValue));
            GetUserAvailabilityResults results = service.getUserAvailability(attendees, new TimeWindow(datearrivee, endValue),
                    AvailabilityData.FreeBusyAndSuggestions, myOptions);
            TimeSuggestion f = results.getSuggestions().iterator().next().getTimeSuggestions().iterator().next();
            Date presentationStart = f.getMeetingTime();

            Calendar cc = Calendar.getInstance();
            c.setTime(presentationStart);
            c.add(Calendar.HOUR, 1); // 30 maximum delai des meetings
            Date presentationEnd;
            presentationEnd = c.getTime();


            Appointment appointment = new  Appointment(service);
            appointment.setSubject("Presentation");
            appointment.setBody(MessageBody.getMessageBodyFromText("Meeting :" + email + manager.getOutlookMail()));
            appointment.setStart(presentationStart);
            appointment.setEnd(presentationEnd);
            appointment.getOptionalAttendees().add(email);
            appointment.getOptionalAttendees().add(manager.getOutlookMail());

            appointment.save();
            Appointmentt newapp = new Appointmentt(email,manager.getAppointmenttId(),presentationStart,presentationEnd,appointment.getId().toString());
            app.save(newapp);

        }
        return true;
    }
*/
@PreAuthorize("hasAnyAuthority('GERER_APPOINTMENTS')")
    @GetMapping("/appointments")
    public Collection<Appointmentt> getAppointmentts() {
        return app.findAll();

    }
    @PreAuthorize("hasAnyAuthority('GERER_APPOINTMENTS')")
    @GetMapping("/appointmentstatus/{code}")
    public String getAppointmentStatus(@PathVariable String code) throws Exception {
        List<CalendarEvent> tts = new ArrayList<>();
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));

        Appointment appointment = Appointment.bind(service, new ItemId(code));
        String rep = appointment.getMyResponseType().toString();
        String reponse = appointment.getMyResponseType().toString();
        return rep;

    }
    @PreAuthorize("hasAnyAuthority('GERER_APPOINTMENTS')")
    @GetMapping("/appointmentcancel/{code}")
    public String cancelApp(@PathVariable String code) throws Exception {
        List<CalendarEvent> tts = new ArrayList<>();
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));

        Appointment appointment = Appointment.bind(service, new ItemId(code));
        appointment.cancelMeeting();
        Appointmentt appo = app.findAppointmenttsByCode(code);
        appo.setReponse("Annulé");
        app.save(appo);
        return "done";
    }
    @PreAuthorize("hasAnyAuthority('GERER_APPOINTMENTS')")
    @GetMapping("/appointments/{email}")
    public List<Appointmentt> getAppointmenttsByMail(@PathVariable String email) {
        return pre.getPresentationOfMail(email);

    }
    @GetMapping("/appsOrdered")
    public Collection<Appointmentt> getAppsPE() {
        return app.getAppsOrderDate(Sort.by("startTime").ascending());

    }
    @GetMapping("/appscr")
    public Collection<AppointmentOutlook> getAppscr() {
        Collection<Appointmentt> appointmentts = app.getAppscr(Sort.by("startTime").ascending());
        Collection<AppointmentOutlook> appointmentOutlooks = new ArrayList<>();

        appointmentts.forEach(appointmentt ->{
                    AppointmentOutlook appointmentOutlook = new AppointmentOutlook();
                    appointmentOutlook.setStartTime(appointmentt.getStartTime());
                    appointmentOutlook.setEndTime(appointmentt.getEndTime());
                    appointmentOutlook.setStatus(appointmentt.getReponse());
                    List<String> attendees = appointmentt.getCollaborateurs().stream().map(c -> c.getNamee()).collect(Collectors.toList());
                    appointmentOutlook.setAttendees(attendees);
                if(appointmentt.getPeriodessai() != null)  {
                    appointmentOutlook.setType("Période d'essai");
                }
                else appointmentOutlook.setType("Présentation");

                    appointmentOutlooks.add(appointmentOutlook);

                }
                );
        return appointmentOutlooks;


    }

    @GetMapping("/appsOutlook/{month}/{year}")
    @PreAuthorize("hasAnyAuthority('GERER_APPOINTMENTS')")
    public Collection<AppointmentOutlook> findAppointmentsOutlook(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable int month,
                                                                  @PathVariable int year) throws Exception {
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));
        CalendarFolder cf=CalendarFolder.bind(service, WellKnownFolderName.Calendar);
        Collection<AppointmentOutlook> appointmentOutlooks = new ArrayList<>();

        LocalDate initial = LocalDate.of(year, month+1, 13);
        LocalDate start = initial.with(firstDayOfMonth());
        Date startDate = java.sql.Date.valueOf(start);
        LocalDate end = initial.with(lastDayOfMonth());
        Date endDate = java.sql.Date.valueOf(end);
     System.out.println(startDate);
        System.out.println(endDate);

        FindItemsResults<Appointment> findResults = cf.findAppointments(new CalendarView(startDate, endDate));
        for (Appointment item : findResults.getItems()) {

            Appointment item2 = Appointment.bind(service, new ItemId(item.getId().getUniqueId()));

            AppointmentOutlook appointmentOutlook = new AppointmentOutlook();
            List<String> att = new ArrayList<>();
            appointmentOutlook.setStartTime(item2.getStart());
            appointmentOutlook.setEndTime(item2.getEnd());
            appointmentOutlook.setTitle(item2.getSubject());
            appointmentOutlook.setStatus("Actif");
            for (Appointmentt a : app.findAll()) {
                if (a.getCodeAppointment() != null) {
                    if (a.getCodeAppointment().equals(item2.getId().getUniqueId())) {
                        if (a.getPeriodessai() != null) {
                            appointmentOutlook.setType("Période d'essai");
                        } else appointmentOutlook.setType("Présentation");
                    }

                }}

                for (Attendee attendee : item2.getRequiredAttendees()) {
                    Optional<Collaborateur> collaborateur = coll.findbyEmail(attendee.getAddress());
                    if (collaborateur.isPresent()) {
                        att.add(collaborateur.get().getNamee());
                    }
                }
                appointmentOutlook.setAttendees(att);
                appointmentOutlooks.add(appointmentOutlook);


            }

        return appointmentOutlooks;

    }

    @PreAuthorize("hasAnyAuthority('GERER_APPOINTMENTS')")
    @GetMapping("/appointment/{EmailID}")
    public Object getAppointmentt(@PathVariable Long EmailID) {
        return app.findById(EmailID);
    }

    /*
        @DeleteMapping("/appointment/{EmailID}")
        public boolean deleteAppointmentt(@PathVariable String EmailID){
            Appointmentt appointment=app.getOne(EmailID);
            app.delete(appointment);;
            return true;
        }
        */

    @PreAuthorize("hasAnyAuthority('GERER_APPOINTMENTS')")
    @DeleteMapping("/appointment/{id}")
    public boolean deleteAppointmenttById(@PathVariable Long id) {
        Appointmentt appointment = app.getOne(id);
        app.delete(appointment);
        ;
        return true;
    }

    @PreAuthorize("hasAnyAuthority('GERER_APPOINTMENTS')")
    @PutMapping("/appointment")

    public Appointmentt UpdateAppointmentt(@RequestBody Appointmentt appointment) {
        return app.save(appointment);
    }

    @PostMapping(value = "/appointment")
    @Transactional
    @PreAuthorize("hasAnyAuthority('GERER_APPOINTMENTS')")
    public Appointmentt CreateAppointmentt(@RequestBody Appointmentt appointment) {
        return app.save(appointment);
    }


    @RequestMapping("/appointmentreport/{code}/{endVal}/{nbDays}")
    @PreAuthorize("hasAnyAuthority('GERER_APPOINTMENTS')")

    public Appointmentt appointmentReportByDays(@PathVariable String code
            , @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") @PathVariable Date endVal, @PathVariable int nbDays) throws Exception {

        List<TimeSuggestion> tts = new ArrayList<>();
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));
        String title = par.getParametreByName("Titre de la présentation").getParametreValue();
        String body = par.getParametreByName("Message de présentation").getParametreValue();
        int duree = Integer.parseInt(par.getParametreByName("durée de présentation").getParametreValue());
        int maxsug = Integer.parseInt(par.getParametreByName("Nombre maximum des suggestions par jour").getParametreValue());

        Appointmentt apppo = app.findAppointmenttsByCode(code);
        Collaborateur nv = apppo.getCollaborateurs().iterator().next();
        Collaborateur manager = apppo.getCollaborateurs().get(1);

        Appointment appoi = Appointment.bind(service, new ItemId(code));
        Presentations presentations = pre.getPresentationByMail(nv.getOutlookMail());
        List<Collaborateur> collsOfAppointment = new ArrayList<>();
        System.out.println(manager.getOutlookMail());

        collsOfAppointment.add(nv);
        collsOfAppointment.add(manager);

        Calendar c = Calendar.getInstance();
        c.setTime(endVal);
        c.add(Calendar.DATE, 30); // 30 maximum delai des meetings
        Date endValue;
        endValue = c.getTime();

        c.setTime(endVal);
        c.add(Calendar.DATE, nbDays); // 30 maximum delai des meetings
        Date sd;
        sd = c.getTime();


        List<AttendeeInfo> attendees = new ArrayList<>();
        attendees.add(new AttendeeInfo(nv.getOutlookMail()));
        attendees.add(new AttendeeInfo(manager.getOutlookMail()));
        attendees.add(new AttendeeInfo(appOutlookMail));


        AvailabilityOptions myOptions = new AvailabilityOptions();
        myOptions.setMeetingDuration(duree);
        myOptions.setMaximumNonWorkHoursSuggestionsPerDay(0);
        myOptions.setMinimumSuggestionQuality(SuggestionQuality.Excellent);
        myOptions.setMaximumSuggestionsPerDay(maxsug);

        GetUserAvailabilityResults results = service.getUserAvailability(attendees, new TimeWindow(sd, endValue), AvailabilityData.FreeBusyAndSuggestions, myOptions);

        results.getSuggestions().forEach(suggestion -> {
            suggestion.getTimeSuggestions().forEach(
                    timeSuggestion -> {
                        Instant current = timeSuggestion.getMeetingTime().toInstant();
                        LocalDateTime ldt = LocalDateTime.ofInstant(current, ZoneId.systemDefault());
                        Instant currentt = sd.toInstant();
                        LocalDateTime ldtt = LocalDateTime.ofInstant(currentt, ZoneId.systemDefault());

                        if (timeSuggestion.getMeetingTime().after(sd) && ldt.getHour() != 12 && ldt.getHour() != 13 && ldt.getHour() != 8) {
                            tts.add(timeSuggestion);
                        }
                    }
            );
        });

       /*  Collection<Date> dates = null;

        for(Suggestion suggestion : results.getSuggestions())
        {
            for(TimeSuggestion timeSuggestion: suggestion.getTimeSuggestions()){
            if(timeSuggestion.getMeetingTime().after(sValue))
            {
            dates.add(timeSuggestion.getMeetingTime());
            }
            }
        }


*/


        System.out.println(results.getSuggestions().iterator().next().getTimeSuggestions().iterator().next().getMeetingTime());
        Date presentationStart = tts.iterator().next().getMeetingTime();
        // Date presentationStart = results.getSuggestions().iterator().next().getTimeSuggestions().iterator().next().getMeetingTime();
        //  Date presentationStart = dates.iterator().next();
        c.setTime(presentationStart);
        c.add(Calendar.MINUTE, duree); // 30 maximum delai des meetings
        Date presentationEnd;
        presentationEnd = c.getTime();
        System.out.println(presentationStart);
        System.out.println(presentationEnd);

        Appointment appointment = new Appointment(service);
        appointment.setSubject(title);
        appointment.setBody(MessageBody.getMessageBodyFromText(body));
        appointment.setStart(presentationStart);
        appointment.setEnd(presentationEnd);
        appointment.getRequiredAttendees().add(nv.getOutlookMail());
        appointment.getRequiredAttendees().add(manager.getOutlookMail());
        appointment.getOptionalAttendees().add(appOutlookMail);
        appointment.save();
//heeere heere heere heere
        Appointmentt newapp = new Appointmentt(presentationStart, presentationEnd, appointment.getId().toString(), collsOfAppointment, presentations);
        app.save(newapp);
        Appointmentt appp = app.findAppointmenttsByCode(code);
        appp.setReponse("Reporté");
        appp.setCodeAppointment(null);
        app.save(appp);
        appoi.cancelMeeting();
        presentations.setAppointment(newapp);
        pre.save(presentations);


        return newapp;
    }
/*
    @Scheduled(fixedRate = 3000000)
    public boolean CheckStatusAutoOfPE() throws Exception {

        List<CalendarEvent> tts = new ArrayList<>();
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));
 Collection<Periodessai> listPeriod = periodessaiService.findAll();
        for(Periodessai periodessai:listPeriod) {
                Instant current = periodessai.getStartDate().toInstant();
                LocalDateTime ldt = LocalDateTime.ofInstant(current, ZoneId.systemDefault());
                if(!periodessai.getEtat().equals("Rupture")) {
                    if (ldt.isAfter(LocalDateTime.now())) {
                        Periodessai periodessaiToSave = periodessaiService.findById(periodessai.getPeriodessaiID()).get();
                        periodessaiToSave.setEtat("En attente");
                        periodessaiService.save(periodessaiToSave);
                    } else {
                        Periodessai periodessaiToSave = periodessaiService.findById(periodessai.getPeriodessaiID()).get();
                        periodessaiToSave.setEtat("En cours");
                        periodessaiService.save(periodessaiToSave);

                    }
                }
            };

        return true;

    }
*/
    @RequestMapping("/appointmentreport/{code}/{endVal}")
    @PreAuthorize("hasAnyAuthority('GERER_APPOINTMENTS')")
    public Appointmentt appointmentReportt(@PathVariable String code, @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") @PathVariable Date endVal) throws Exception {

        List<TimeSuggestion> tts = new ArrayList<>();
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));
        String title = par.getParametreByName("Titre de la présentation").getParametreValue();
        String body = par.getParametreByName("Message de présentation").getParametreValue();
        int duree = Integer.parseInt(par.getParametreByName("durée de présentation").getParametreValue());
        int maxsug = Integer.parseInt(par.getParametreByName("Nombre maximum des suggestions par jour").getParametreValue());


        Appointment appoi = Appointment.bind(service, new ItemId(code));


        Appointmentt apppo = app.findAppointmenttsByCode(code);
        Collaborateur nv = apppo.getPresentations().getNewCollaborateur();
        System.out.println(nv.getOutlookMail());

        Collaborateur manager = apppo.getCollaborateurs().get(1);
        Presentations presentations = pre.getPresentationByMail(nv.getOutlookMail());
        List<Collaborateur> collsOfAppointment = new ArrayList<>();
        System.out.println(manager.getOutlookMail());

        collsOfAppointment.add(nv);
        collsOfAppointment.add(manager);


        Calendar c = Calendar.getInstance();
        c.setTime(endVal);
        c.add(Calendar.DATE, 30); // 30 maximum delai des meetings
        Date endValue;
        endValue = c.getTime();


        List<AttendeeInfo> attendees = new ArrayList<>();
        attendees.add(new AttendeeInfo(nv.getOutlookMail()));
        attendees.add(new AttendeeInfo(manager.getOutlookMail()));
        attendees.add(new AttendeeInfo(appOutlookMail));


        AvailabilityOptions myOptions = new AvailabilityOptions();
        myOptions.setMeetingDuration(duree);
        myOptions.setMaximumNonWorkHoursSuggestionsPerDay(0);
        myOptions.setMinimumSuggestionQuality(SuggestionQuality.Excellent);
        myOptions.setMaximumSuggestionsPerDay(maxsug);


        GetUserAvailabilityResults results = service.getUserAvailability(attendees, new TimeWindow(endVal, endValue), AvailabilityData.FreeBusyAndSuggestions, myOptions);

        results.getSuggestions().forEach(suggestion -> {
            suggestion.getTimeSuggestions().forEach(timeSuggestion -> {
                        Instant current = timeSuggestion.getMeetingTime().toInstant();
                        LocalDateTime ldt = LocalDateTime.ofInstant(current, ZoneId.systemDefault());
                        Instant currentt = endVal.toInstant();
                        LocalDateTime ldtt = LocalDateTime.ofInstant(currentt, ZoneId.systemDefault());

                        if (timeSuggestion.getMeetingTime().after(endVal) && ldt.getHour() != 12 && ldt.getHour() != 13 && ldt.getHour() != 8) {
                            tts.add(timeSuggestion);
                        }
                    }
            );
        });

      /*   Collection<Date> dates = null;

        for(Suggestion suggestion : results.getSuggestions())
        {
            for(TimeSuggestion timeSuggestion: suggestion.getTimeSuggestions()){
            if(timeSuggestion.getMeetingTime().after(sValue))
            {
            dates.add(timeSuggestion.getMeetingTime());
            }
            }
        }

*/


        System.out.println(results.getSuggestions().iterator().next().getTimeSuggestions().iterator().next().getMeetingTime());
        Date presentationStart = tts.iterator().next().getMeetingTime();
        // Date presentationStart = results.getSuggestions().iterator().next().getTimeSuggestions().iterator().next().getMeetingTime();
        //  Date presentationStart = dates.iterator().next();
        c.setTime(presentationStart);
        c.add(Calendar.MINUTE, duree); // 30 maximum delai des meetings
        Date presentationEnd;
        presentationEnd = c.getTime();
        System.out.println(presentationStart);
        System.out.println(presentationEnd);

        Appointment appointment = new Appointment(service);
        appointment.setSubject(title);
        appointment.setBody(MessageBody.getMessageBodyFromText(body));
        appointment.setStart(presentationStart);
        appointment.setEnd(presentationEnd);
        appointment.getRequiredAttendees().add(nv.getOutlookMail());
        appointment.getRequiredAttendees().add(manager.getOutlookMail());
        appointment.getOptionalAttendees().add(appOutlookMail);
        appointment.save();

        Appointmentt newapp = new Appointmentt(presentationStart, presentationEnd, appointment.getId().toString(), collsOfAppointment, presentations);
        app.save(newapp);
        Appointmentt appp = app.findAppointmenttsByCode(code);
        appp.setReponse("Reporté");
        appp.setCodeAppointment(null);

        app.save(appp);
        appoi.cancelMeeting();
        presentations.setAppointment(newapp);
        pre.save(presentations);


        return newapp;
    }


    @RequestMapping("/check")
    public boolean CheckStatus(@RequestBody List<Appointmentt> appointments) throws Exception {

        List<CalendarEvent> tts = new ArrayList<>();
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));
        for (Appointmentt appointment : appointments) {

            if (appointment.getReponse() == null || appointment.getReponse() == "Accepté") {
                try {
                    Appointment appoi = Appointment.bind(service, new ItemId(appointment.getCodeAppointment()));
                    for (Attendee attendee : appoi.getRequiredAttendees()) {
                        if (attendee.getResponseType() == MeetingResponseType.Decline) {
                            appointment.setReponse("Refusé");
                            app.save(appointment);
                        }
                    }
                } catch (ServiceResponseException e) {
                    appointment.setReponse("Annulé");
                    appointment.setCodeAppointment(null);

                    app.save(appointment);
                    continue;
                }

            }
        }
        return true;

    }

    @Scheduled(fixedRate = 300000)
    public boolean CheckStatusAuto() throws Exception {

        Collection<Appointmentt> appointments = app.findAll();
        List<CalendarEvent> tts = new ArrayList<>();
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));
        for (Appointmentt appointment : appointments) {
            int i = 0;

            /*if(appointment.getReponse() == "Accepté")
            {
                if(appointment.getEndTime().before(DateTime.now().toDate()))
                {
                    appointment.setReponse("Passed");
                    app.save(appointment);
                }
               else if(appointment.getStartTime().before(DateTime.now().toDate()) && appointment.getEndTime().after(DateTime.now().toDate()))
                {
                appointment.setReponse("En cours");
                app.save(appointment);
                }
            }
*/
            if (appointment.getReponse() == null || appointment.getReponse().equals("Accepté") ||
                    appointment.getReponse().equals("une personne à accepté") || appointment.getReponse().equals("Refusé") || appointment.getReponse().equals("accepté par tout le monde")) {

                try {
                    Appointment appoi = Appointment.bind(service, new ItemId(appointment.getCodeAppointment()));
                    for (Attendee attendee : appoi.getRequiredAttendees()) {
                        if (attendee.getResponseType() == MeetingResponseType.Decline) {
                            appointment.setReponse("Refusé");
                            app.save(appointment);
                            break;
                        } else if (attendee.getResponseType() == MeetingResponseType.Accept) {
                            i++;
                        }

                    }

                    if (i == 2) {
                        appointment.setReponse("tout le monde a accepté");
                        app.save(appointment);
                    } else if (i == 1) {
                        appointment.setReponse("une personne a accepté");
                        app.save(appointment);
                    }

                } catch (ServiceResponseException e) {
                    appointment.setReponse("Annulé");
                    appointment.setCodeAppointment(null);

                    app.save(appointment);
                    continue;
                }

            }

        }

        return true;

    }


    @GetMapping("sugg/{nv}/{email}/{startValue}/{endValue}")
    @PreAuthorize("hasAnyAuthority('GERER_APPOINTMENTS')")
    public List<Mapp> planningmanuelSugs(@PathVariable String nv, @PathVariable List<String> email, @PathVariable Date startValue, @PathVariable Date endValue) throws Exception {
        List<List<TimeSuggestion>> result = new ArrayList<>();
        Map<String, List<TimeSuggestion>> map = new HashMap<>();
        List<Mapp> lista = new ArrayList<>();
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));
        String title = par.getParametreByName("Titre de la présentation").getParametreValue();
        String body = par.getParametreByName("Message de présentation").getParametreValue();
        int duree = Integer.parseInt(par.getParametreByName("durée de présentation").getParametreValue());
        int maxsug = Integer.parseInt(par.getParametreByName("Nombre maximum des suggestions par jour").getParametreValue());


        for (String e : email) {
            List<TimeSuggestion> tts = new ArrayList<>();
            Mapp m = new Mapp();
            List<AttendeeInfo> attendees = new ArrayList<AttendeeInfo>();
            attendees.add(new AttendeeInfo(e));
            attendees.add(new AttendeeInfo(nv));
            attendees.add(new AttendeeInfo(appOutlookMail));
            Calendar c = Calendar.getInstance();
            c.setTime(endValue);
            c.add(Calendar.DATE, 1);
            endValue = c.getTime();
            AvailabilityOptions myOptions = new AvailabilityOptions();
            myOptions.setMeetingDuration(duree);
            myOptions.setMaximumNonWorkHoursSuggestionsPerDay(0);
            myOptions.setMinimumSuggestionQuality(SuggestionQuality.Excellent);
            myOptions.setMaximumSuggestionsPerDay(maxsug);
            myOptions.setDetailedSuggestionsWindow(new TimeWindow(startValue, endValue));
            GetUserAvailabilityResults results = service.getUserAvailability(attendees, new TimeWindow(startValue, endValue),
                    AvailabilityData.FreeBusyAndSuggestions, myOptions);

            results.getSuggestions().forEach(suggestion -> {
                suggestion.getTimeSuggestions().forEach(timeSuggestion -> {

                            Instant current = timeSuggestion.getMeetingTime().toInstant();
                            LocalDateTime ldt = LocalDateTime.ofInstant(current, ZoneId.systemDefault());
                            Instant currentt = startValue.toInstant();
                            LocalDateTime ldtt = LocalDateTime.ofInstant(currentt, ZoneId.systemDefault());

                            if (timeSuggestion.getMeetingTime().after(startValue) && ldt.getHour() != 12 && ldt.getHour() != 13 && ldt.getHour() != 8) {
                                tts.add(timeSuggestion);
                            }
                        }
                );
            });
            Optional<Collaborateur> co = coll.findbyEmail(e);
            String nvv = co.get().getNamee();
            m.setNv(e);
            m.setTs(tts);
            lista.add(m);

            result.add(tts);
        }

/*
		for (Suggestion sugg : results.getSuggestions()) {
			for (TimeSuggestion ts  : sugg.getTimeSuggestions() )
			{i
				f(ts.getMeetingTime().before(endValue) && ts.getMeetingTime().after(startValue)) {
            tts.add(ts);
				}

			}
}
*/
        System.out.println(map);
        return lista;
    }


    @RequestMapping("/appmanuel/{nv}/{mn}/{presentationStart}")
    @PreAuthorize("hasAnyAuthority('GERER_APPOINTMENTS')")
    public Appointmentt setAppointManu(@PathVariable String nv, @PathVariable String mn,
                                       @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") @PathVariable Date presentationStart) throws Exception {

        List<TimeSuggestion> tts = new ArrayList<>();
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));
        String title = par.getParametreByName("Titre de la présentation").getParametreValue();
        String body = par.getParametreByName("Message de présentation").getParametreValue();
        int duree = Integer.parseInt(par.getParametreByName("durée de présentation").getParametreValue());


        Presentations presentationPlan = new Presentations();
        Optional<Collaborateur> newCollaborateur = coll.findbyEmail(nv);
        Optional<Collaborateur> man = coll.findbyEmail(mn);

        List<Collaborateur> collsOfAppointment = new ArrayList<>();

        if (pre.getPresentationByMail(nv) == null) {
            presentationPlan.setNewCollaborateur(newCollaborateur.get());

            pre.save(presentationPlan);
        } else {
            presentationPlan = pre.getPresentationByMail(nv);
        }

        Calendar c = Calendar.getInstance();
        c.setTime(presentationStart);
        c.add(Calendar.MINUTE, duree); // 30 maximum delai des meetings
        Date presentationEnd;
        presentationEnd = c.getTime();
        System.out.println(presentationStart);
        System.out.println(presentationEnd);

        Appointment appointment = new Appointment(service);
        appointment.setSubject(title);
        appointment.setBody(MessageBody.getMessageBodyFromText(body + " " + nv + " and " + mn));
        appointment.setStart(presentationStart);
        appointment.setEnd(presentationEnd);
        appointment.getRequiredAttendees().add(mn);
        appointment.getRequiredAttendees().add(nv);
        appointment.getOptionalAttendees().add(appOutlookMail);
        appointment.save();
        collsOfAppointment.add(newCollaborateur.get());
        collsOfAppointment.add(man.get());


        List<Appointmentt> appointments = new ArrayList<>();
        Appointmentt newapp = new Appointmentt(presentationStart, presentationEnd, appointment.getId().toString(), collsOfAppointment, presentationPlan);
        app.save(newapp);
        appointments.add(newapp);
        presentationPlan.setAppointments(appointments);
        pre.save(presentationPlan);


        return newapp;
    }

}
