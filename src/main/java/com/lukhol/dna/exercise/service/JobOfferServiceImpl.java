package com.lukhol.dna.exercise.service;

import com.lukhol.dna.exercise.dto.JobOfferDto;
import com.lukhol.dna.exercise.errors.ServiceValidationException;
import com.lukhol.dna.exercise.model.Category;
import com.lukhol.dna.exercise.model.JobOffer;
import com.lukhol.dna.exercise.model.User;
import com.lukhol.dna.exercise.repository.CategoryRepository;
import com.lukhol.dna.exercise.repository.JobOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobOfferServiceImpl implements JobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public JobOffer create(JobOfferDto jobOfferDto) {
        Category category = categoryRepository
                .findById(jobOfferDto.getCategoryId())
                .orElseThrow(() -> new ServiceValidationException("Category with provided id does not exists."));

        if (!isValid(jobOfferDto.getTitle(), jobOfferDto.getCompanyName())) {
            throw new ServiceValidationException("Any fields cannot be empty.");
        }

        JobOffer jobOffer = new JobOffer();
        jobOffer.setCategory(category);
        jobOffer.setCompanyName(jobOfferDto.getCompanyName());
        jobOffer.setFromDate(jobOfferDto.getFrom());
        jobOffer.setToDate(jobOfferDto.getTo());
        jobOffer.setTitle(jobOfferDto.getTitle());

        return jobOfferRepository.save(jobOffer);
    }

    @Override
    public Optional<JobOffer> findById(Long id) {
        return jobOfferRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JobOfferDto> findDtoById(Long id) {
        return jobOfferRepository
                .findById(id)
                .map(this::jobOfferToDto);
    }

    @Override
    public List<JobOfferDto> findAll() {
        return jobOfferRepository
                .findAll() //TODO: Omitted state check
                .stream()
                .map(this::jobOfferToDto)
                .collect(Collectors.toList());
    }

    private boolean isValid(String... values) {
        for (String val : values) {
            if (StringUtils.isEmpty(val))
                return false;
        }

        return true;
    }

    private JobOfferDto jobOfferToDto(JobOffer jobOffer) {
        User user = jobOffer.getCreatedBy();

        return JobOfferDto.builder()
                .title(jobOffer.getTitle())
                .categoryId(jobOffer.getCategory().getId())
                .companyName(jobOffer.getCompanyName())
                .from(jobOffer.getFromDate())
                .to(jobOffer.getToDate())
                .createdById(user != null ? user.getId() : 0)
                .build();
    }
}