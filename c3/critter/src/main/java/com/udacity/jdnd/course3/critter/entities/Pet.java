package com.udacity.jdnd.course3.critter.entities;

import com.udacity.jdnd.course3.critter.pet.PetType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Enumerated
    private PetType type;

    private String name;

    private LocalDate birthDate;

    private String notes;

    @ManyToOne(targetEntity = Customer.class, optional = false)
    private Customer owner;
}
