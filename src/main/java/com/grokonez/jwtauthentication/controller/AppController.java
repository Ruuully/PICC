package com.grokonez.jwtauthentication.controller;

import com.grokonez.jwtauthentication.model.Collaborateur;
import com.grokonez.jwtauthentication.model.Presentations;
import com.grokonez.jwtauthentication.model.appointment.Appointmentt;
import com.grokonez.jwtauthentication.service.AppointmentService;
import com.grokonez.jwtauthentication.service.CollaborateurService;
import com.grokonez.jwtauthentication.service.ParametreService;
import com.grokonez.jwtauthentication.service.PlanPresentationsService;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.availability.AvailabilityData;
import microsoft.exchange.webservices.data.core.enumeration.availability.SuggestionQuality;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.misc.availability.AttendeeInfo;
import microsoft.exchange.webservices.data.misc.availability.AvailabilityOptions;
import microsoft.exchange.webservices.data.misc.availability.GetUserAvailabilityResults;
import microsoft.exchange.webservices.data.misc.availability.TimeWindow;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.availability.CalendarEvent;
import microsoft.exchange.webservices.data.property.complex.availability.TimeSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/serv")
@PreAuthorize("hasAnyAuthority('GERER_PERIODESSAI')")
public class AppController {

    @Autowired
    private AppointmentService app;
    @Autowired
    private CollaborateurService coll;

    @Autowired
    private PlanPresentationsService pre;

    @Autowired
    private ParametreService par;

    @Value("${spring.outlook.mail}")
    private String appOutlookMail;

    @Value("${spring.outlook.password}")
    private String appOutlookPassword;


  /*
	ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
	ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
	service.setCredentials(credentials);
	service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx")); */

    @GetMapping("disp/{email}/{startValue}/{endValue}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<CalendarEvent> dispp(@PathVariable String email, @PathVariable Date startValue, @PathVariable Date endValue) throws Exception {

        List<CalendarEvent> tts = new ArrayList<>();
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));
        List<AttendeeInfo> attendees = new ArrayList<AttendeeInfo>();
        attendees.add(new AttendeeInfo(email));
        Calendar c = Calendar.getInstance();
        c.setTime(endValue);
        c.add(Calendar.DATE, 1);
        endValue = c.getTime();
        GetUserAvailabilityResults results = service.getUserAvailability(attendees, new TimeWindow(startValue, endValue),
                AvailabilityData.FreeBusyAndSuggestions);

        results.getAttendeesAvailability().forEach(attendeeAvailability -> {
            attendeeAvailability.getCalendarEvents()
                    .forEach(calendarEvent -> tts.add(calendarEvent));
        });

		/*
		for (AttendeeAvailability attendeeAvailability : results.getAttendeesAvailability()) {				
				for (CalendarEvent calendarEvent : attendeeAvailability.getCalendarEvents()) {	
					if (calendarEvent.getStartTime().before(endValue) && calendarEvent.getStartTime().after(startValue))
					{
                tts.add(calendarEvent);
					}
			}
		}
		*/
        return tts;
    }


    /*@GetMapping("disp/{email}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String test(@PathVariable String email)
    {
    return "Controller works";
    }
    */
    @GetMapping("sugg/{email}/{startValue}/{endValue}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<TimeSuggestion> suggestion(@PathVariable List<String> email, @PathVariable Date startValue, @PathVariable Date endValue) throws Exception {

        List<TimeSuggestion> tts = new ArrayList<>();
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));
        List<AttendeeInfo> attendees = new ArrayList<AttendeeInfo>();
        for (String t : email) {
            attendees.add(new AttendeeInfo(t));
        }
        Calendar c = Calendar.getInstance();
        c.setTime(endValue);
        c.add(Calendar.DATE, 1);
        endValue = c.getTime();
        AvailabilityOptions myOptions = new AvailabilityOptions();
        myOptions.setMeetingDuration(60);
        myOptions.setMaximumNonWorkHoursSuggestionsPerDay(0);
        myOptions.setMinimumSuggestionQuality(SuggestionQuality.Good);
        myOptions.setMaximumSuggestionsPerDay(5);
        myOptions.setDetailedSuggestionsWindow(new TimeWindow(startValue, endValue));
        GetUserAvailabilityResults results = service.getUserAvailability(attendees, new TimeWindow(startValue, endValue),
                AvailabilityData.FreeBusyAndSuggestions, myOptions);

        results.getSuggestions().forEach(suggestion -> {
            suggestion.getTimeSuggestions().forEach(timeSuggestion -> tts.add(timeSuggestion));
        });
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
        return tts;
    }

    @GetMapping("suggappoint/{email}/{startValue}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String setAppoint(@PathVariable String email, @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") @PathVariable Date startValue) throws Exception {

        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));


        ////////////////
        Appointment appointment = new Appointment(service);
        appointment.setSubject("new appointment test");
        appointment.setBody(MessageBody.getMessageBodyFromText("Test Body Msg"));
		
		/*
		   Instant instant = startValue.toInstant(ZoneOffset.UTC);
		    Date sv = Date.from(instant);
		    Instant instantt = endValue.toInstant(ZoneOffset.UTC);
		    Date ev = Date.from(instantt);
		    		 */
        Calendar c = Calendar.getInstance();
        c.setTime(startValue);
        c.add(Calendar.HOUR, 1);
        Date endValue = c.getTime();
        appointment.setStart(startValue);
        appointment.setEnd(endValue);
        appointment.getOptionalAttendees().add(email);
        appointment.save();

        return "done";
    }


    @RequestMapping("appointment/{email}/{datearrivee}")
    public boolean appoint(@PathVariable String email, @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") @PathVariable Date datearrivee) throws Exception {

        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));
        String title = par.getParametreByName("Titre de la présentation").getParametreValue();
        String body = par.getParametreByName("Message de présentation").getParametreValue();
        int duree = Integer.parseInt(par.getParametreByName("durée de présentation").getParametreValue());
        int maxsug = Integer.parseInt(par.getParametreByName("Nombre maximum des suggestions par jour").getParametreValue());


        List<Collaborateur> managers = coll.findAllManagers();
        Optional<Collaborateur> newCollaborateur = coll.findbyEmail(email);
        Presentations presentationPlan = new Presentations();

        if (pre.getPresentationByMail(email) == null) {
            presentationPlan.setNewCollaborateur(newCollaborateur.get());

            pre.save(presentationPlan);
        } else {
            presentationPlan = pre.getPresentationByMail(email);
        }

        List<Appointmentt> appointments = new ArrayList<>();
        for (Collaborateur manager : managers) {
            if (!manager.getCollaborateurId().equals(coll.findbyEmail(email).get().getCollaborateurId())) {
                Calendar c = Calendar.getInstance();
                c.setTime(datearrivee);
                c.add(Calendar.DATE, 30); // 30 maximum delai des meetings
                Date endValue;
                endValue = c.getTime();

                List<Collaborateur> collsOfAppointment = new ArrayList<>();
                collsOfAppointment.add(newCollaborateur.get());
                collsOfAppointment.add(manager);


                List<AttendeeInfo> attendees = new ArrayList<>();
                attendees.add(new AttendeeInfo(email));
                attendees.add(new AttendeeInfo(manager.getOutlookMail()));
                attendees.add(new AttendeeInfo(appOutlookMail));

                List<TimeSuggestion> tts = new ArrayList<>();

                AvailabilityOptions myOptions = new AvailabilityOptions();
                myOptions.setMeetingDuration(duree);
                myOptions.setMaximumNonWorkHoursSuggestionsPerDay(0);
                myOptions.setMinimumSuggestionQuality(SuggestionQuality.Excellent);
                myOptions.setMaximumSuggestionsPerDay(maxsug);
                myOptions.setDetailedSuggestionsWindow(new TimeWindow(datearrivee, endValue));
                GetUserAvailabilityResults results = service.getUserAvailability(attendees, new TimeWindow(datearrivee, endValue),
                        AvailabilityData.FreeBusyAndSuggestions, myOptions);
                Calendar cal = Calendar.getInstance();

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
                appointment.getRequiredAttendees().add(email);
                appointment.getRequiredAttendees().add(manager.getOutlookMail());
                appointment.getOptionalAttendees().add(appOutlookMail);

                appointment.save();
                Appointmentt newapp = new Appointmentt(presentationStart, presentationEnd, appointment.getId().toString(), collsOfAppointment, presentationPlan);
                appointments.add(newapp);
                app.save(newapp);

            }
            presentationPlan.setAppointments(appointments);
            pre.save(presentationPlan);

        }
        return true;
    }

    @RequestMapping("appointment/replanifier")
    public boolean replanifier(@RequestBody Collaborateur collaborateur) throws Exception {

        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2013);
        ExchangeCredentials credentials = new WebCredentials(appOutlookMail, appOutlookPassword);
        service.setCredentials(credentials);
        service.setUrl(new URI("https://mail.ilemgroup.com/ews/exchange.asmx"));
        String title = par.getParametreByName("Titre de la présentation").getParametreValue();
        String body = par.getParametreByName("Message de présentation").getParametreValue();
        int duree = Integer.parseInt(par.getParametreByName("durée de présentation").getParametreValue());
        int maxsug = Integer.parseInt(par.getParametreByName("Nombre maximum des suggestions par jour").getParametreValue());


        Collection<Appointmentt> oldAppointments = coll.findbyEmail(collaborateur.getOutlookMail()).get().getPresentations().getAppointments();

        List<Collaborateur> managers = coll.findAllManagers();
        Optional<Collaborateur> newCollaborateur = coll.findbyEmail(collaborateur.getOutlookMail());
        Presentations presentationPlan = new Presentations();

        if (pre.getPresentationByMail(collaborateur.getOutlookMail()) == null) {
            presentationPlan.setNewCollaborateur(newCollaborateur.get());

            pre.save(presentationPlan);
        } else {
            presentationPlan = pre.getPresentationByMail(collaborateur.getOutlookMail());
        }

        List<Appointmentt> appointments = new ArrayList<>();
        for (Collaborateur manager : managers) {
            if (!manager.getCollaborateurId().equals(collaborateur.getCollaborateurId())) {
                Calendar c = Calendar.getInstance();
                c.setTime(collaborateur.getDateArrivee());
                c.add(Calendar.DATE, 30); // 30 maximum delai des meetings
                Date endValue;
                endValue = c.getTime();

                List<Collaborateur> collsOfAppointment = new ArrayList<>();
                collsOfAppointment.add(newCollaborateur.get());
                collsOfAppointment.add(manager);


                List<AttendeeInfo> attendees = new ArrayList<>();
                attendees.add(new AttendeeInfo(collaborateur.getOutlookMail()));
                attendees.add(new AttendeeInfo(manager.getOutlookMail()));
                attendees.add(new AttendeeInfo(appOutlookMail));

                List<TimeSuggestion> tts = new ArrayList<>();

                AvailabilityOptions myOptions = new AvailabilityOptions();
                myOptions.setMeetingDuration(duree);
                myOptions.setMaximumNonWorkHoursSuggestionsPerDay(0);
                myOptions.setMinimumSuggestionQuality(SuggestionQuality.Excellent);
                myOptions.setMaximumSuggestionsPerDay(maxsug);
                myOptions.setDetailedSuggestionsWindow(new TimeWindow(collaborateur.getDateArrivee(), endValue));
                GetUserAvailabilityResults results = service.getUserAvailability(attendees, new TimeWindow(collaborateur.getDateArrivee(), endValue),
                        AvailabilityData.FreeBusyAndSuggestions, myOptions);
                Calendar cal = Calendar.getInstance();

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
                appointment.getRequiredAttendees().add(collaborateur.getOutlookMail());
                appointment.getRequiredAttendees().add(manager.getOutlookMail());
                appointment.getOptionalAttendees().add(appOutlookMail);

                appointment.save();
                Appointmentt newapp = new Appointmentt(presentationStart, presentationEnd, appointment.getId().toString(), collsOfAppointment, presentationPlan);
                appointments.add(newapp);
                app.save(newapp);

            }
            presentationPlan.setAppointments(appointments);
            pre.save(presentationPlan);
            oldAppointments.forEach(appointmentt -> {
                appointmentt.setReponse("Annulé");
                app.save(appointmentt);
                Appointment appointmentOutlook = null;
                try {
                    appointmentOutlook = Appointment.bind(service, new ItemId(appointmentt.getCodeAppointment()));
                    appointmentOutlook.cancelMeeting();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

        }
            return true;

    }

}

	

