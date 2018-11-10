package com.lukhol.dna.exercise;
//
//import com.lukhol.dna.exercise.dto.JobOfferDto;
//import com.lukhol.dna.exercise.model.JobOffer;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Date;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class ExerciseApplicationTests {
//
//	@Autowired
//	private TestRestTemplate restTemplate;

//	@Test
//	public void canGetOffers_empty() {
//		var response = restTemplate.getForEntity("/job/offers", JobOfferDto[].class);
//
//		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//		assertThat(response.getBody()).isEmpty();
//	}
//
//	@Test
//	@Sql("/offers.sql")
//	public void canGetOffers_offersExists() {
//		var response = restTemplate.getForEntity("/job/offers", JobOfferDto[].class);
//
//		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//		assertThat(response.getBody()).hasSize(1);
//	}
//
//	@Test
//	@WithMockUser(username = "lukhol")
//	public void canPostOffers() {
//		JobOfferDto offer = new JobOfferDto();
//		offer.setCategoryId(1);
//		offer.setCompanyName("DNA");
//		offer.setCreatedById(1);
//		offer.setFrom(new Date());
//		offer.setTo(new Date());
//		offer.setTitle("Fullstack Java/JavaScript developer.");
//
//		var response = restTemplate.postForLocation("/job/offers", offer);
//
//		assertThat(response.toString()).isEqualToIgnoringCase("");
//	}
//}