package com.lukhol.dna.exercise.model;

import com.lukhol.dna.exercise.model.base.StateAuditable;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends StateAuditable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "myNative")
    private Long id;
    private String login;

    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}