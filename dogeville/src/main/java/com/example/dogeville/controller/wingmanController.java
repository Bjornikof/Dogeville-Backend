package com.example.dogeville.controller;

import com.example.dogeville.model.TokenResponse;
import com.example.dogeville.model.Wingman;
import com.example.dogeville.model.WingmanAuth;
import com.example.dogeville.service.wingmanService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

@CrossOrigin(origins = "http://localhost:4200",  allowedHeaders = "*")
@RequestMapping("/")
@RestController
public class wingmanController {

    private final wingmanService service;

    public wingmanController(wingmanService service) {
        this.service = service;
    }

    @RequestMapping("/wingman/mail/{mail}")
    public boolean doesMailExist(@PathVariable String mail) {
        return service.doesMailExist(mail);
    }

    @RequestMapping("/wingman/find/{mail}")
    public Wingman getWingmanByMail(@PathVariable String mail) {
        return service.getWingmanByMail(mail);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/wingman")
    public void addWingman(@RequestBody @Valid Wingman wm) {
        service.addWingman(wm);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/wingman/edit")
    public Wingman editWingman(@Param("mail") String mail, @Param("name") String name, @Param("surname") String surname, @Param("gender") String gender, @Param("birth") String birth
            , @Param("edu") String edu, @Param("job") String job, @Param("country") String country, @Param("state") String state, @Param("county") String county) throws ParseException {
        System.out.println(""+name+ surname+ gender+ birth+ edu+ job+ country+ state+ county+ mail);
        return service.editWingman(mail, name, surname, gender, birth, edu, job, country, state, county);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/wingman/register")
    public void registerWingman(@RequestBody @Valid Wingman wm) throws UnsupportedEncodingException, MessagingException {
        service.registerWingman(wm);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/wingman/reset")
    public void startReset(@RequestBody @Valid Wingman wm) throws UnsupportedEncodingException, MessagingException {
        service.resetPassword(wm);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/wingman/verify")
    public boolean verifyAccount(@Param("code") String code) {
        boolean verified = service.verify(code);
        return verified;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/wingman/login")
    public ResponseEntity<TokenResponse> loginWingman(@RequestBody WingmanAuth authRequest) {
        return service.loginWingman(authRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/wingman/photo/upload")
    public @ResponseBody boolean uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
       return service.uploadImage(file);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/wingman/photo/download")
    public String downloadImage(@Param("mail") String mail) {
        return service.downloadImage(mail);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/wingman/changemail")
    public boolean updateMail(@Param("mail") String mail, @Param("newmail") String newmail, @Param("password") String password) {
        return service.updateMail(mail, newmail, password);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/wingman/changepw")
    public boolean updatePassword(@Param("mail") String mail, @Param("newpassword") String newpassword, @Param("password") String password) {
        return service.updatePassword(mail, newpassword, password);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/wingman/password")
    public boolean updatePasswordWithCode(@Param("code") String code, @Param("newpassword") String newpassword) {
        return service.updatePasswordWithCode(code, newpassword);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/wingman/login/ver/{mail}")
    public String loginVerification(@PathVariable String mail) throws ParseException {
        return service.loginVerification(mail);
    }


}
