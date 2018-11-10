package com.lukhol.dna.exercise.repository;

import com.lukhol.dna.exercise.BaseTest;
import com.lukhol.dna.exercise.model.User;
import com.lukhol.dna.exercise.model.base.EntityState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserRepositoryTests extends BaseTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() {
        user = User.builder().login("lukhol").build();
        user.setState(EntityState.PERSISTED);
    }

    @Test
    @Transactional
    public void canFindById_persistedState() {
        persistAndFlush(user);

        Optional<User> received = userRepository.findPersistedById(user.getId());
        assertThat(received).isNotEmpty();
        assertThat(received).isPresent();
    }

    @Test
    @Transactional
    public void cannotFindById_deletedState() {
        User user = User.builder().login("lukhol").build();
        user.setState(EntityState.DELETED);
        persistAndFlush(user);

        Optional<User> received = userRepository.findPersistedById(user.getId());
        assertThat(received).isNotPresent();
    }

    @Test
    @Transactional
    public void canFindByLogin_persistedState() {
        persistAndFlush(user);

        Optional<User> received = userRepository.findPersistedByLogin("lukhol");
        assertThat(received).isPresent();
    }

    @Test
    @Transactional
    public void cannotFindByLogin_deletedState() {
        user.setState(EntityState.DELETED);
        persistAndFlush(user);

        Optional<User> received = userRepository.findPersistedByLogin("lukhol");
        assertThat(received).isNotPresent();
    }

    @Test
    @Transactional
    public void canFindAllPersisted() {
        user.setState(EntityState.DELETED);
        persistAndFlush(user);

        IntStream
                .range(0, 120)
                .forEach(i -> {
                    User u = new User("lukhol" + i, "password");
                    u.setState(EntityState.PERSISTED);
                    persistAndFlush(u);
                });

        assertThat(userRepository.findAllPersisted()).hasSize(120);
        assertThat(userRepository.findAll()).hasSize(121);
    }

    @Test
    @Transactional
    public void canCheckIfLoginIsUnique_isNotUnique() {
        persistAndFlush(user);
        assertThat(userRepository.isLoginUnique(user.getLogin())).isEqualTo(false);
    }

    @Test
    @Transactional
    public void canCheckIfLoginIsUnique_isUnique() {
        persistAndFlush(user);
        assertThat(userRepository.isLoginUnique("lukhol2")).isEqualTo(true);
    }
}
