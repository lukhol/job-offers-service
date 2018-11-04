package com.lukhol.dna.exercise.service;

import com.lukhol.dna.exercise.dto.UserDto;
import com.lukhol.dna.exercise.model.User;
import com.lukhol.dna.exercise.repository.UserRepository;
import com.lukhol.dna.exercise.errors.ServiceValidationException;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository
                .findAllPersisted()
                .stream()
                .map(this::hidePassword)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository
                .findPersistedById(id)
                .map(this::hidePassword);
    }

    @Override
    @Transactional
    public User create(UserDto userDto) {
        if(StringUtils.isEmpty(userDto.getLogin()) || StringUtils.isEmpty(userDto.getPassword()))
            throw new ServiceValidationException("Username and password cannot be empty.");

        if(!userRepository.isLoginUnique(userDto.getLogin()))
            throw new ServiceValidationException("This login is already occupied. Choose another one.");

        User user = new User();
        user.setLogin(userDto.getLogin());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user = userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public User update(Long id, UserDto userDto) throws NotFoundException {
        User user = userRepository
                .findPersistedById(id)
                .orElseThrow(() -> new NotFoundException("Not found user with id: " + id));

        if(StringUtils.isEmpty(userDto.getLogin()) || StringUtils.isEmpty(userDto.getPassword()))
            throw new ServiceValidationException("Username and password cannot be empty.");

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setLogin(user.getLogin());

        return user;
    }

    @Override
    @Transactional
    public void removeUserById(Long id) throws NotFoundException {
        User user = userRepository
                .findPersistedById(id)
                .orElseThrow(() -> new NotFoundException("Not found user with id: " + id));

        userRepository.softDelete(user);
    }

    private User hidePassword(User user) {
        user.setPassword("You ain't gonna get it.");
        return user;
    }
}