package com.lukhol.dna.exercise.controller;

import com.lukhol.dna.exercise.dto.UserDto;
import com.lukhol.dna.exercise.model.User;
import com.lukhol.dna.exercise.security.UserPrincipal;
import com.lukhol.dna.exercise.service.UserService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUsers() {
        log.info("{} GET /users ", getClass().getSimpleName());
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable final Long id) throws NotFoundException {
        log.info("GET /users/{id} ", id);

        User user = userService
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Not found user with id: " + id));

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody final UserDto userDto) {
        log.info("POST /users {}", userDto.toString());

        User user = userService.create(userDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody final UserDto userDto,
                                        @PathVariable final Long id) throws NotFoundException {
        log.info("PUT /users/{id} {}", id, userDto);

        User user = userService.update(id, userDto);
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable final Long id) throws NotFoundException {
        log.info("DELETE /users/{id} ", id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            User currentUser = ((UserPrincipal) authentication.getPrincipal()).getUser();
            userService.removeUserById(id, currentUser);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}