package com.berge.ratenow.testapplication.entities.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
}
