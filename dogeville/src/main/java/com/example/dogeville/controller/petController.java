package com.example.dogeville.controller;

import com.example.dogeville.model.Pet;
import com.example.dogeville.model.Wingman;
import com.example.dogeville.service.petService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RequestMapping("/")
@RestController
public class petController {

    private final petService service;

    public petController(petService service) {
        this.service = service;
    }

    @RequestMapping("/pet/find/dog/{wm}")
    public List<Pet> getPetsDByWingman(@PathVariable Wingman wm) {
        return service.getPetsDByWingman(wm);
    }

    @RequestMapping("/pet/find/cat/{wm}")
    public List<Pet> getPetsCByWingman(@PathVariable Wingman wm) {
        return service.getPetsCByWingman(wm);
    }

    @RequestMapping("/pet/search")
    public List<Pet> petSearch(@Param("type") String type, @Param("breed") String breed, @Param("gender") String gender,
                               @Param("country") String country, @Param("state") String state, @Param("county") String county) {
        return service.searchPet(type, breed, gender, country, state, county);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/pet")
    public void addPet(@RequestBody @Valid Pet pet) {
        service.addPet(pet);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/pet/edit")
    public void editPet(@RequestBody @Valid Pet pet) {
        service.editPet(pet);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/pet/find/{id}")
    public Pet getPet(@PathVariable String id) {
        return service.getPet(id);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/pet/delete/{id}")
    public void deletePet(@PathVariable String id) {
        service.deletePet(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/pet/photo/upload")
    public @ResponseBody
    boolean uploadPetImage(@RequestParam("file") MultipartFile file) throws IOException {
        return service.uploadPetImage(file);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/pet/photo/download")
    public String downloadPetImage(@Param("id") String id) {
        return service.downloadPetImage(id);
    }
}
