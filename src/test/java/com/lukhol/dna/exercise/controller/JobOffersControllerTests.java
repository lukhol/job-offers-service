package com.lukhol.dna.exercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukhol.dna.exercise.dto.JobOfferDto;
import com.lukhol.dna.exercise.model.JobOffer;
import com.lukhol.dna.exercise.model.User;
import com.lukhol.dna.exercise.repository.UserRepository;
import com.lukhol.dna.exercise.service.JobOfferService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = JobOfferController.class, secure = false)
public class JobOffersControllerTests {

    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private JobOfferService jobOfferService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<List<String>> userIdsCaptor;

    @Captor
    private ArgumentCaptor<List<String>> categoryIdsCaptor;

    @Test
    public void canGetAll_success() throws Exception {
        var offers = List.of(JobOfferDto.builder().title("IT").companyName("company").createdById(1L).build());
        given(jobOfferService.findAll()).willReturn(offers);

        mockMvc.perform(MockMvcRequestBuilders.get("/job/offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void canAdd_success() throws Exception {
        given(userRepository.findPersistedById(any())).willReturn(Optional.of(User.builder().id(1L).login("lukhol").build()));
        given(jobOfferService.create(any())).willReturn(new JobOffer());

        var perform = MockMvcRequestBuilders
                .post("/job/offers")
                .content(mapper.writeValueAsString(new JobOfferDto()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(perform)
                .andExpect(status().isCreated());
    }

    @Test
    public void canFindByUserAndCategory_success() throws Exception {
        var userIdsCaptor = ArgumentCaptor.forClass(List.class);
        var categoryIdsCaptor = ArgumentCaptor.forClass(List.class);

        given(jobOfferService.findByUserIdsAndCategoryIds(
                userIdsCaptor.capture(), categoryIdsCaptor.capture())
        ).willReturn(List.of());

        var perform = MockMvcRequestBuilders
                .get("/job/offers/search")
                .param("userId", "1")
                .param("userId", "3")
                .param("categoryId", "23")
                .param("categoryId", "45");

        mockMvc
                .perform(perform)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        assertThat(userIdsCaptor.getValue()).hasSize(2);
        assertThat(userIdsCaptor.getValue()).contains(1L, 3L);

        assertThat(categoryIdsCaptor.getValue()).hasSize(2);
        assertThat(categoryIdsCaptor.getValue()).contains(23L, 45L);
    }
}