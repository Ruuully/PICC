package com.grokonez.jwtauthentication.controller.CollaborateurMetier;

import com.grokonez.jwtauthentication.model.Metier;
import com.grokonez.jwtauthentication.service.MetierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Collection;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/apim")
@PreAuthorize("hasAnyAuthority('GERER_METIER')")

public class MetierController {
    @Autowired
    private MetierService metierService;

    @GetMapping("/metiers")
    public Collection<Metier> getMetiers() {
        return metierService.findAll();
    }

    @GetMapping(value = "/metier/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Object getMetier(@PathVariable Long id) {
        return metierService.findById(id);
    }

    @DeleteMapping("/metier/{id}")
    public boolean deleteMetier(@PathVariable Long id) {
        Metier metier = metierService.getOne(id);
        metierService.delete(metier);
        ;
        return true;
    }


    @PutMapping("/metier")
    public Metier UpdateMetier(@RequestBody Metier metier) {
        return metierService.save(metier);
    }

    @PostMapping("/metier")
    @Transactional
    public Metier CreateMetier(@RequestBody Metier metier) {
        return metierService.save(metier);
    }
}
