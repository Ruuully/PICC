package com.grokonez.jwtauthentication.controller.CollaborateurMetier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grokonez.jwtauthentication.controller.AppController;
import com.grokonez.jwtauthentication.controller.PeriodessaiController;
import com.grokonez.jwtauthentication.model.Collaborateur;
import com.grokonez.jwtauthentication.model.Periodessai;
import com.grokonez.jwtauthentication.service.CollaborateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/apic")
@PreAuthorize("hasAnyAuthority('GERER_COLLABORATEUR')")

public class CollaborateurController {

    @Autowired
    private AppController appController;

    @Autowired
    private CollaborateurService collaborateurService;

    @Autowired
    private PeriodessaiController periodessaiController;

    @GetMapping("/collaborateurs")
    public Collection<Collaborateur> getCollaborateurs() {
        return collaborateurService.findAll();

    }

    @GetMapping("/managers")
    public List<Collaborateur> getManagers() {
        return collaborateurService.findAllManagers();

    }

    @GetMapping("/newCs")
    public List<Collaborateur> getNewCs() {
        return collaborateurService.findNewColls();

    }

    @GetMapping("/collaborateur/{EmailID}")
    public Object getCollaborateur(@PathVariable Long EmailID) {
        return collaborateurService.findById(EmailID);
    }

    /*
        @DeleteMapping("/collaborateur/{EmailID}")
        public boolean deleteCollaborateur(@PathVariable String EmailID){
            Collaborateur collaborateur=collaborateurService.getOne(EmailID);
            collaborateurService.delete(collaborateur);;
            return true;
        }
        */
    @DeleteMapping("/collaborateur/{id}")
    public boolean deleteCollaborateurById(@PathVariable Long id) {
        collaborateurService.deleteById(id);
        ;
        return true;
    }


    @PostMapping("/collaborateurUpdate")
    @JsonIgnoreProperties(value = "")
    public Collaborateur UpdateCollaborateur(@RequestBody Collaborateur collaborateur) {
        return collaborateurService.update(collaborateur);
    }
    /* @GetMapping("/checkemail/{email}")

     public boolean checkIfEmailExists(@PathVariable String email) {
         return collaborateurService.checkIfEmailExists(email);
     }*/
    @PostMapping(value = "/collaborateur")
    @JsonIgnoreProperties({"metier", "manager"})
    @Transactional
    public Collaborateur CreateCollaborateur(@RequestBody Collaborateur collaborateur) throws Exception {
        if(collaborateur.isNewCol()) {
            Collaborateur collaborateurSaved = collaborateurService.save(collaborateur);
            periodessaiController.CreatePeriodessai(new Periodessai(collaborateur.getDateArrivee(), collaborateur));
            appController.appoint(collaborateur.getOutlookMail(), collaborateur.getDateArrivee());
            return collaborateurSaved;
        }
        else {return collaborateurService.save(collaborateur);}

    }
 /*   @PostMapping(value = "/collaborateur/{metierId}")
    public Collaborateur CreateCollaborateurById(@PathVariable Long metierId,@RequestBody Collaborateur collaborateur){

   Metier metier = metierrepository.findOne(metierId);
        return collaborateur;
    }*/
}
