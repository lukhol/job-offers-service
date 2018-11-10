package com.lukhol.dna.exercise.model;

import com.lukhol.dna.exercise.model.base.StateAuditable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class JobOffer extends StateAuditable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "myNative")
    private Long id;

    @ManyToOne
    private Category category;

    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    //@Column(columnDefinition = "DATETIME(3)", updatable = false, nullable = false)
    private Date fromDate;

    @Temporal(TemporalType.TIMESTAMP)
    //@Column(columnDefinition = "DATETIME(3)", updatable = false, nullable = false)
    private Date toDate;
    private String companyName;
}