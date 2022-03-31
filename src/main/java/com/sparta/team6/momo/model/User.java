package com.sparta.team6.momo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Entity
public class User {
    @Column(nullable = false, unique = true)
    @NotEmpty
    private String email;

    @Column(nullable = false)
    @NotEmpty
    private String password;
}
