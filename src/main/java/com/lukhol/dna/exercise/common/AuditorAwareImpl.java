package com.lukhol.dna.exercise.common;

import com.lukhol.dna.exercise.model.User;
import com.lukhol.dna.exercise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component("auditorAware")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuditorAwareImpl implements AuditorAware<User> {

    private final UserRepository userRepository;

    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            return Optional.empty();

        String userIdString = auth.getName();
        try {
            long userId = Long.parseLong(userIdString);
            return userRepository.findById(userId);
        } catch (Exception e) {
            log.error("Error occurred during auditing user.");
        }

        return Optional.empty();
    }
}