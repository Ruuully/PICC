package com.grokonez.jwtauthentication.controller.parametre;

import com.grokonez.jwtauthentication.model.Parametre;
import com.grokonez.jwtauthentication.service.ParametreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/apiparametre")
@PreAuthorize("hasAnyAuthority('GERER_PARAMETRES')")

public class ParametreController {
    @Autowired
    private ParametreService parametreService;

    @GetMapping("/parametres")
    public Collection<Parametre> getParametres() {
        return parametreService.findAll();
    }

    @GetMapping("/parametre/{name}")
    public Parametre getParametre(@PathVariable String name) {
        return parametreService.getParametreByName(name);
    }
    /*
        @DeleteMapping("/parametre/{EmailID}")
        public boolean deleteParametre(@PathVariable String EmailID){
            Parametre parametre=parametreService.getOne(EmailID);
            parametreService.delete(parametre);;
            return true;
        }
        */

    @PutMapping("/parametre")

    public Parametre UpdateParametre(@RequestBody Parametre parametre) {
        return parametreService.save(parametre);
    }

    @PostMapping(value = "/parametre")
    public Parametre CreateParametre(@RequestBody Parametre parametre) {
        return parametreService.save(parametre);
    }
}
