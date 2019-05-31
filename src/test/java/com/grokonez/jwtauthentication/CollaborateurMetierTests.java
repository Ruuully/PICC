package com.grokonez.jwtauthentication;

import com.grokonez.jwtauthentication.DAO.CollaborateurDao;
import com.grokonez.jwtauthentication.DAO.MetierDAO;
import com.grokonez.jwtauthentication.model.Collaborateur;
import com.grokonez.jwtauthentication.model.Metier;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class CollaborateurMetierTests {


    @Resource
    private MetierDAO daom;

    @Resource
    private CollaborateurDao daoc;


    @Test
    public void testSaveMetier() {
        Metier metier = new Metier("tteesstt");
        Metier savedmet = daom.save(metier);
        Assert.assertEquals(savedmet.getName(), "tteesstt");


    }

    @Test
    public void testSaveCollaborateur() {
        Metier metier = new Metier("tteesstt");
        Metier savedmet = daom.save(metier);
        Collaborateur collaborateur = new Collaborateur("anas@ilemgroup.com", "Anas", false, metier);
        Collaborateur savedCollaborateur = daoc.save(collaborateur);
        Assert.assertEquals(savedCollaborateur.getMetier().getName(), "tteesstt");

    }

}
