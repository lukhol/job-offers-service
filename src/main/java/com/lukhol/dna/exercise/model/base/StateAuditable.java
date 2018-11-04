package com.lukhol.dna.exercise.model.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class StateAuditable<U> {

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    //@Column(columnDefinition = "DATETIME(3)", updatable = false, nullable = false)
    protected Date creationDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    //@Column(columnDefinition = "DATETIME(3)")
    protected Date lastModifiedDate;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    protected U createdBy;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    protected U lastModifiedBy;


    @Enumerated(EnumType.STRING)
    protected EntityState state = EntityState.PERSISTED;
}