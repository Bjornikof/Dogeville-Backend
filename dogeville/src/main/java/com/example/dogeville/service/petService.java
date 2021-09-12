package com.example.dogeville.service;

import com.example.dogeville.model.Pet;
import com.example.dogeville.model.Wingman;
import com.example.dogeville.repository.petRepository;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class petService {

    private final petRepository repo;
    private final wingmanService wm;
    Gson gson = new Gson();

    public petService(petRepository repo, wingmanService wm) {
        this.repo = repo;
        this.wm = wm;
    }

    public List<Pet> getPetsDByWingman(Wingman wm) {
        return repo.findAllByWmidAndPtype(wm, "dog");
    }

    public List<Pet> getPetsCByWingman(Wingman wm) {
        return repo.findAllByWmidAndPtype(wm, "cat");
    }

    public Pet getPetById(UUID id) {
        return repo.findPetByPid(id);
    }

    public Pet getPet(String id) {
        UUID uuid = UUID.fromString(id);
        return getPetById(uuid);
    }

    public List<Pet> searchPet(String type, String breed, String gender, String country, String state, String county) {
        //all type
        if (breed.equals("null") && gender.equals("null") && country.equals("null") && state.equals("null") && county.equals("null")) {
            return repo.findAllByPtype(type);
        }
        //all specific type, breed, gender
        else if (!breed.equals("null") && !gender.equals("null") && country.equals("null") && state.equals("null") && county.equals("null")) {
            return repo.findAllByPtypeAndPbreedAndPgender(type, breed, gender);
        }
        //all specific type, breed
        else if (!breed.equals("null") && gender.equals("null") && country.equals("null") && state.equals("null") && county.equals("null")) {
            return repo.findAllByPtypeAndPbreed(type, breed);
        }
        //all specific type, gender
        else if (breed.equals("null") && !gender.equals("null") && country.equals("null") && state.equals("null") && county.equals("null")) {
            return repo.findAllByPtypeAndPgender(type, gender);
        }
        //all specific type, country
        else if (breed.equals("null") && gender.equals("null") && !country.equals("null") && state.equals("null") && county.equals("null")) {
           return repo.findAllByPtypeAndWmidWmcountry(type, country);
        }
        //all specific type, gender, country
        else if (breed.equals("null") && !gender.equals("null") && !country.equals("null") && state.equals("null") && county.equals("null")) {
            return repo.findAllByPtypeAndPgenderAndWmidWmcountry(type, gender, country);
        }
        //all specific type, breed, country
        else if (!breed.equals("null") && gender.equals("null") && !country.equals("null") && state.equals("null") && county.equals("null")) {
            return repo.findAllByPtypeAndPbreedAndWmidWmcountry(type, breed, country);
        }
        //all specific type, breed, gender, country
        else if (!breed.equals("null") && !gender.equals("null") && !country.equals("null") && state.equals("null") && county.equals("null")) {
            return repo.findAllByPtypeAndPbreedAndPgenderAndWmidWmcountry(type, breed, gender, country);
        }
        //all specific type, country, state
        else if (breed.equals("null") && gender.equals("null") && !country.equals("null") && !state.equals("null") && county.equals("null")) {
            return repo.findAllByPtypeAndWmidWmcountryAndWmidWmstate(type, country, state);
        }
        //all specific type, breed, country, state
        else if (!breed.equals("null") && gender.equals("null") && !country.equals("null") && !state.equals("null") && county.equals("null")) {
            return repo.findAllByPtypeAndPbreedAndWmidWmcountryAndWmidWmstate(type, breed, country, state);
        }
        //all specific type, breed, country, state, county
        else if (!breed.equals("null") && gender.equals("null") && !country.equals("null") && !state.equals("null") && !county.equals("null")) {
            return repo.findAllByPtypeAndPbreedAndWmidWmcountryAndWmidWmstateAndWmidWmcounty(type, breed, country, state, county);
        }
        //all specific type, gender, country, state
        else if (breed.equals("null") && !gender.equals("null") && !country.equals("null") && !state.equals("null") && county.equals("null")) {
            return repo.findAllByPtypeAndPgenderAndWmidWmcountryAndWmidWmstate(type, gender, country, state);
        }
        //all specific type, gender, country, state, county
        else if (breed.equals("null") && !gender.equals("null") && !country.equals("null") && !state.equals("null") && !county.equals("null")) {
            return repo.findAllByPtypeAndPgenderAndWmidWmcountryAndWmidWmstateAndWmidWmcounty(type, gender, country, state, county);
        }
        //all specific type, breed, gender, country, state, county
        else if (!breed.equals("null") && !gender.equals("null") && !country.equals("null") && !state.equals("null") && county.equals("null")) {
            return repo.findAllByPtypeAndPbreedAndPgenderAndWmidWmcountryAndWmidWmstate(type, breed, gender, country, state);
        }
        //all specific type, country, state, county (whole address)
        else if (breed.equals("null") && gender.equals("null") && !country.equals("null") && !state.equals("null") && !county.equals("null")) {
            return repo.findAllByPtypeAndWmidWmcountryAndWmidWmstateAndWmidWmcounty(type, country, state, county);
        } //all specific
        else if (!breed.equals("null") && !gender.equals("null") && !country.equals("null") && !state.equals("null") && !county.equals("null")) {
            return repo.findAllByPtypeAndPbreedAndPgenderAndWmidWmcountryAndWmidWmstateAndWmidWmcounty(type, breed, gender, country, state, county);
        }
        else{
            return null;
        }
    }

    public void editPet(Pet pet) {
        repo.saveAndFlush(pet);
    }

    public void deletePet(String id) {
        UUID uuid = UUID.fromString(id);
        repo.deleteById(uuid);
    }

    public void addPet(Pet pet) {
        repo.saveAndFlush(pet);
    }

    public boolean uploadPetImage(MultipartFile file) throws IOException {
        byte[] image = file.getBytes();

        Pet old = getPetById(UUID.fromString(file.getOriginalFilename()));
        old.setPphoto(image);
        repo.saveAndFlush(old);
        return true;
    }

    public String downloadPetImage(String id) {
        UUID uuid = UUID.fromString(id);
        Pet old = getPetById(uuid);
        if (old.getPphoto() == null) {
            return null;
        } else {
            byte[] retrievedImage = old.getPphoto();
            String json = gson.toJson(retrievedImage);
            return json;
        }
    }
}
