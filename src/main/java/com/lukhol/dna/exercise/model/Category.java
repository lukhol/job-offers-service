package com.lukhol.dna.exercise.model;

import com.lukhol.dna.exercise.model.base.StateAuditable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class Category extends StateAuditable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<JobOffer> jobOffers = new HashSet<>();
}