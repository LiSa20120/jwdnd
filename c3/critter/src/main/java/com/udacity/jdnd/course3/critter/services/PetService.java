package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {
    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    public Pet savePet(Pet pet, Long ownerId) {
        Customer owner = customerRepository.getOne(ownerId);
        pet.setOwner(owner);
        pet = petRepository.save(pet);
        owner.insertPet(pet);
        customerRepository.save(owner);
        return pet;
    }

    public Pet getPetById(Long id) {
        return petRepository.getOne(id);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwnerId(Long id) {
        return petRepository.getAllByOwnerId(id);
    }
}
