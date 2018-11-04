package com.lukhol.dna.exercise.repository;

import com.lukhol.dna.exercise.model.User;
import com.lukhol.dna.exercise.repository.base.CustomRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CustomRepository<User, Long> {

    @Query("select u from User u where u.id = :id and u.state = 'PERSISTED'")
    Optional<User> findPersistedById(@Param("id") Long id);

    @Query("select u from User u where u.login = :login and u.state = 'PERSISTED'")
    Optional<User> findPersistedByLogin(@Param("login") String login);

    @Query("select u from User u where u.state ='PERSISTED'")
    List<User> findAllPersisted();

    @Query("select case when count(u) <= 0 then true else false end from User u where u.login = :login")
    boolean isLoginUnique(@Param("login") String login);
}