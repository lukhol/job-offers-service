package com.lukhol.dna.exercise.controller;

import com.lukhol.dna.exercise.dto.JobOfferDto;
import com.lukhol.dna.exercise.model.JobOffer;
import com.lukhol.dna.exercise.service.JobOfferService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/job/offers")
@RequiredArgsConstructor
public class JobOfferController {

    private final JobOfferService jobOfferService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(jobOfferService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOfferById(@PathVariable final Long id) throws NotFoundException {
        log.info("GET /job/offers/{id} ", id);

        JobOfferDto jobOfferDto = jobOfferService
                .findDtoById(id)
                .orElseThrow(() -> new NotFoundException("Not found job offer with id: " + id));

        return ResponseEntity.ok(jobOfferDto);
    }

    @PostMapping
    public ResponseEntity<?> createOffer(@RequestBody final JobOfferDto jobOfferDto) {
        log.info("POST /job/offers ({});", jobOfferDto.toString());

        JobOffer jobOffer = jobOfferService.create(jobOfferDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/job/offers/{id}")
                .buildAndExpand(jobOffer.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> findByUserIdsAndCategoryIds(@RequestParam("userId") final List<Long> userIds,
                                                         @RequestParam("categoryId") final List<Long> categoryIds) {
        log.info("GET /job/offers/search (userIds: {}, categoryIds: {});", userIds.toString(), categoryIds.toString());

        return ResponseEntity.ok(jobOfferService.findByUserIdsAndCategoryIds(userIds, categoryIds));
    }
}