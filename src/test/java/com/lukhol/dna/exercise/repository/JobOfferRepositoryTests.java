package com.lukhol.dna.exercise.repository;

import com.lukhol.dna.exercise.BaseTest;
import com.lukhol.dna.exercise.model.Category;
import com.lukhol.dna.exercise.model.JobOffer;
import com.lukhol.dna.exercise.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JobOfferRepositoryTests extends BaseTest {

    @Autowired
    private JobOfferRepository repository;

    private JobOffer offer1;
    private JobOffer offer2;

    @Before
    public void setUp() {
        //prepareCategories();
        prepareUsers();
        prepareJobOffers();
    }

    @Test
    public void canFind_isInDb() {
        var ids = List.of(1L);
        var offers = repository.findByUserIdsAndCategoryIds(ids, ids.size(), ids, ids.size());
        assertThat(offers).hasSize(1);

        JobOffer offer = offers.get(0);
        assertThat(offer.getCategory().getId()).isEqualTo(1L);
        assertThat(offer.getCompanyName()).isEqualTo("UpMenu");
        assertThat(offer.getTitle()).isEqualTo("Java developer");
    }

    @Test
    public void cannotFind_notInDb() {
        var userIds = List.of(1L, 2L, 4L);
        var categoryIds = List.of(3L, 5L, 8L);

        var offers = repository.findByUserIdsAndCategoryIds(userIds, userIds.size(), categoryIds, categoryIds.size());
        assertThat(offers).isEmpty();
    }

    private void prepareCategories() {
        var categoriesNames = List.of("IT", "Food & Drinks", "Office", "Courier", "Shop assistant");
        for (String catName : categoriesNames) {
            Category c = new Category();
            c.setName(catName);
            persistAndFlush(c);
        }
    }

    private void prepareUsers() {
        IntStream.of(1, 2, 3)
                .forEach(i -> {
                    User user = new User("user" + i, "password" + i);
                    persistAndFlush(user);
                });
    }

    private void prepareJobOffers() {
        offer1 = new JobOffer();
        offer1.setTitle("Java developer");
        offer1.setCompanyName("UpMenu");
        offer1.setCategory(entityManager.find(Category.class, 1L));
        offer1.setFromDate(new Date());
        offer1.setToDate(new Date(new Date().getTime() + 300000));
        offer1.setCreatedBy(entityManager.getReference(User.class, 1L));

        offer2 = new JobOffer();
        offer2.setTitle("Waiter");
        offer2.setCompanyName("McDonald");
        offer2.setCategory(entityManager.find(Category.class, 2L));
        offer2.setFromDate(new Date());
        offer2.setToDate(new Date(new Date().getTime() + 300000));
        offer1.setCreatedBy(entityManager.getReference(User.class, 1L));

        persistAndFlush(offer1, offer2);
    }
}