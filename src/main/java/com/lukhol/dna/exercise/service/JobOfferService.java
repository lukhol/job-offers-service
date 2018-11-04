package com.lukhol.dna.exercise.service;

import com.lukhol.dna.exercise.dto.JobOfferDto;
import com.lukhol.dna.exercise.model.JobOffer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface JobOfferService {

    JobOffer create(JobOfferDto jobOfferDto);

    Optional<JobOffer> findById(Long id);

    Optional<JobOfferDto> findDtoById(Long id);

    List<JobOfferDto> findAll();

    List<JobOfferDto> findByUserIdsAndCategoryIds(List<Long> userIds, List<Long> categoryIds);
}