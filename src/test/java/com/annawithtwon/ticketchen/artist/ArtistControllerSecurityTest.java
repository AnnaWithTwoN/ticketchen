package com.annawithtwon.ticketchen.artist;

import com.annawithtwon.ticketchen.account.AccountService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(ArtistController.class)
public class ArtistControllerSecurityTest {
    private final String UrlPath = "/artists";

    @MockBean
    private ArtistService artistService;
    @MockBean
    private AccountService accountService;
    @MockBean
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldAllowAccessToGetPaths() throws Exception {
        mockMvc.perform(
                        get(UrlPath)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void shouldForbidAccessWithoutAuthorizationToken() throws Exception {
        mockMvc.perform(
                post(UrlPath)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldForbidAccessToRegularRole() throws Exception {
        String adminToken = "Bearer " + JWT.create()
                .withSubject("username")
                .withClaim("role", "regular")
                .sign(Algorithm.HMAC256("poke".getBytes()));

        mockMvc.perform(
                        post(UrlPath)
                                .header("Authorization", adminToken)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldAllowAccessToAdminRole() throws Exception {
        String adminToken = "Bearer " + JWT.create()
                .withSubject("username")
                .withClaim("role", "admin")
                .sign(Algorithm.HMAC256("poke".getBytes()));
        when(artistService.createArtist(any())).thenAnswer(i -> i.getArguments()[0]);

        mockMvc.perform(
                        post(UrlPath)
                                .header("Authorization", adminToken)
                                .content("{\"name\": \"any name\"}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
 }
