package com.lukhol.dna.exercise.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lukhol.dna.exercise.ExerciseApplication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobOfferDto {

    private String title;
    private long categoryId;
    private String companyName;

    @JsonFormat(pattern = ExerciseApplication.DATE_FORMAT)
    private Date from;

    @JsonFormat(pattern = ExerciseApplication.DATE_FORMAT)
    private Date to;

    private long createdById;
}