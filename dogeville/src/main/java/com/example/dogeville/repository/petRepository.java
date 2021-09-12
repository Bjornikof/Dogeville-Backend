package com.example.dogeville.repository;

import com.example.dogeville.model.Pet;
import com.example.dogeville.model.Wingman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface petRepository extends JpaRepository<Pet, UUID> {
    List<Pet> findAllByWmidAndPtype(Wingman wm, String type);

    Pet findPetByPid(UUID id);

    List<Pet> findAllByPtype(String type);

    List<Pet> findAllByPtypeAndPbreed(String type, String breed);

    List<Pet> findAllByPtypeAndPbreedAndPgender(String type, String breed, String gender);

    List<Pet> findAllByPtypeAndPgender(String type, String gender);

    List<Pet> findAllByPtypeAndWmidWmcountry(String type, String country);

    List<Pet> findAllByPtypeAndPgenderAndWmidWmcountry(String type, String gender, String country);

    List<Pet> findAllByPtypeAndPbreedAndWmidWmcountry(String type, String breed, String country);

    List<Pet> findAllByPtypeAndPbreedAndPgenderAndWmidWmcountry(String type, String breed, String gender, String country);

    List<Pet> findAllByPtypeAndWmidWmcountryAndWmidWmstate(String type, String country, String state);

    List<Pet> findAllByPtypeAndPbreedAndWmidWmcountryAndWmidWmstate(String type, String breed, String country, String state);

    List<Pet> findAllByPtypeAndPgenderAndWmidWmcountryAndWmidWmstate(String type, String gender, String country, String state);

    List<Pet> findAllByPtypeAndPbreedAndPgenderAndWmidWmcountryAndWmidWmstate(String type, String breed, String gender, String country, String state);

    List<Pet> findAllByPtypeAndPbreedAndWmidWmcountryAndWmidWmstateAndWmidWmcounty(String type, String breed, String country, String state, String county);

    List<Pet> findAllByPtypeAndPgenderAndWmidWmcountryAndWmidWmstateAndWmidWmcounty(String type, String gender, String country, String state, String county);

    List<Pet> findAllByPtypeAndWmidWmcountryAndWmidWmstateAndWmidWmcounty(String type, String country, String state, String county);

    List<Pet> findAllByPtypeAndPbreedAndPgenderAndWmidWmcountryAndWmidWmstateAndWmidWmcounty(String type, String breed, String gender, String country, String state, String county);
}

