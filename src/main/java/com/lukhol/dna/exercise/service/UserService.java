package com.lukhol.dna.exercise.service;

import com.lukhol.dna.exercise.dto.UserDto;
import com.lukhol.dna.exercise.model.User;
import javassist.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    Optional<User> findById(Long id);

    User create(UserDto userDto);

    User update(Long id, UserDto userDto) throws NotFoundException;

    void removeUserById(Long id) throws NotFoundException;
}
