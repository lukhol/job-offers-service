package com.lukhol.dna.exercise.service;

import com.lukhol.dna.exercise.dto.JobOfferDto;
import com.lukhol.dna.exercise.errors.ServiceValidationException;
import com.lukhol.dna.exercise.model.Category;
import com.lukhol.dna.exercise.model.JobOffer;
import com.lukhol.dna.exercise.model.User;
import com.lukhol.dna.exercise.repository.CategoryRepository;
import com.lukhol.dna.exercise.repository.JobOfferRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobOfferServiceTests {

    @Mock
    private JobOfferRepository jobOfferRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private JobOfferService jobOfferService;

    @Before
    public void setUp() {
        jobOfferService = new JobOfferServiceImpl(jobOfferRepository,categoryRepository);
    }

    @Test
    public void cannotCreate_notValid() {
        expectedEx.expect(ServiceValidationException.class);
        expectedEx.expectMessage("Any fields cannot be empty.");

        JobOfferDto dto = JobOfferDto.builder().categoryId(1L).companyName("").build();

        jobOfferService.create(dto);
    }

    @Test
    public void cannotCreate_categoryNotFound() {
        expectedEx.expect(ServiceValidationException.class);
        expectedEx.expectMessage("Category with provided id does not exists.");

        JobOfferDto dto = JobOfferDto.builder().companyName("company").title("title").build();

        jobOfferService.create(dto);
    }

    @Test
    public void canCreate_success() {
        Category category = Category.builder().id(1L).name("IT").build();
        JobOfferDto dto = JobOfferDto.builder().companyName("company").title("title").categoryId(1L).build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(jobOfferRepository.save(any(JobOffer.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        var actual = jobOfferService.create(dto);

        assertThat(actual).isNotNull();
        assertThat(actual.getTitle()).isEqualTo("title");
        assertThat(actual.getCompanyName()).isEqualTo("company");
    }

    @Test
    public void canFindDtoById_success() {
        Date from = new Date();
        Date to = new Date(from.getTime() + 2500);

        JobOffer offer = new JobOffer();
        offer.setId(1L);
        offer.setCategory(Category.builder().id(1L).name("category").build());
        offer.setTitle("Developer");
        offer.setCompanyName("Company");
        offer.setCreatedBy(User.builder().id(2L).login("login").password("password").build());
        offer.setFromDate(from);
        offer.setToDate(to);

        when(jobOfferRepository.findById(anyLong())).thenReturn(Optional.of(offer));

        var actual = jobOfferService.findDtoById(1L);

        assertThat(actual).isPresent();
        assertThat(actual.get().getCompanyName()).isEqualTo("Company");
        assertThat(actual.get().getTitle()).isEqualTo("Developer");
        assertThat(actual.get().getCategoryId()).isEqualTo(1L);
        assertThat(actual.get().getFrom()).isEqualTo(from);
        assertThat(actual.get().getTo()).isEqualTo(to);
        assertThat(actual.get().getCreatedById()).isEqualTo(2L);
    }
}