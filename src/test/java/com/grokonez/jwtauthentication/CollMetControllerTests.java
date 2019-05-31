package com.grokonez.jwtauthentication;

import com.grokonez.jwtauthentication.DAO.CollaborateurDao;
import com.grokonez.jwtauthentication.DAO.MetierDAO;
import com.grokonez.jwtauthentication.controller.CollaborateurMetier.CollaborateurController;
import com.grokonez.jwtauthentication.model.Collaborateur;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CollaborateurController.class, secure = false)
@ActiveProfiles("test")

public class CollMetControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CollaborateurDao collaborateurDao;
    @MockBean
    private MetierDAO metierDAO;


    @Test
    public void testGetAll() throws Exception {
        List<Collaborateur> list = new ArrayList<>();
        Collaborateur c1 = new Collaborateur("outlook@gmail.com", "anas");
        Collaborateur c2 = new Collaborateur("outlookk@gmail.com", "anass");
        list.add(c1);
        list.add(c2);

        Mockito.when(collaborateurDao.findAll())
                .thenReturn(list);


        mockMvc.perform(get("/apic/collaborateurs")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testgetManagers() throws Exception {
        List<Collaborateur> list = new ArrayList<>();
        Collaborateur c1 = new Collaborateur("outlook@gmail.com", "anas");
        Collaborateur c2 = new Collaborateur("outlookk@gmail.com", "anass");
        list.add(c1);
        list.add(c2);

        Mockito.when(collaborateurDao.findAllManagers())
                .thenReturn(list);


        mockMvc.perform(get("/apic/managers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
