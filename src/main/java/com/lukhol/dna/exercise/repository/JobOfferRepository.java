package com.lukhol.dna.exercise.repository;

import com.lukhol.dna.exercise.model.JobOffer;
import com.lukhol.dna.exercise.repository.base.CustomRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobOfferRepository extends CustomRepository<JobOffer, Long> {

    @Query("select distinct jo from JobOffer jo left join fetch jo.category c where (:userIdsSize > 0 and jo.createdBy.id in (:userIds)) and (:categoryIdsSize > 0 and c.id in (:categoryIds))")
    List<JobOffer> findByUserIdsAndCategoryIds(@Param("userIds") List<Long> userIds,
                                               @Param("userIdsSize") int userIdsSize,
                                               @Param("categoryIds") List<Long> categoryIds,
                                               @Param("categoryIdsSize") int categoryIdsSize);
}