package com.lukhol.dna.exercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukhol.dna.exercise.dto.JobOfferDto;
import com.lukhol.dna.exercise.model.JobOffer;
import com.lukhol.dna.exercise.model.User;
import com.lukhol.dna.exercise.repository.UserRepository;
import com.lukhol.dna.exercise.security.JwtTokenProvider;
import com.lukhol.dna.exercise.security.UserPrincipal;
import com.lukhol.dna.exercise.service.JobOfferService;
import com.lukhol.test.config.WithMockCustomUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobOffersControllerTestsSecure {

    private String AUTH_HEADER = "Bearer ";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private JobOfferService jobOfferService;

    @MockBean
    private UserRepository userRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    /**
     * With use of @WithMockCustomUser - jwtAuthorizationFilter is throwing exception and processing filter
     * without setting security context. SecurityContext is set by @WithMockCustomUser.
     */
    @Test
    @WithMockCustomUser
    public void canAdd_success() throws Exception {
        given(jobOfferService.create(any())).willReturn(new JobOffer());

        var perform = MockMvcRequestBuilders
                .post("/job/offers")
                .content(mapper.writeValueAsString(new JobOfferDto()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(perform)
                .andExpect(status().isCreated());
    }

    /**
     * Token is generated in the test and passed as Header. Then filter is validating token and setting
     * security context. Another approach to above one.
     */
    @Test
    public void canAdd_success2() throws Exception {
        given(jobOfferService.create(any())).willReturn(new JobOffer());
        when(userRepository.findPersistedById(1L)).thenReturn(
                Optional.of(User.builder().id(1L).login("login").password("password").build())
        );

        var user = User.builder().id(1L).login("login").password("password").build();
        var userPrincipal = new UserPrincipal(user);
        var authToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        var jwtToken = new JwtTokenProvider().generateToken(authToken);

        var perform = MockMvcRequestBuilders
                .post("/job/offers")
                .content(mapper.writeValueAsString(new JobOfferDto()))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", AUTH_HEADER + jwtToken);

        mockMvc.perform(perform)
                .andExpect(status().isCreated());
    }
}