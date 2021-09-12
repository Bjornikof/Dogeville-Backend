package com.example.dogeville.service;

import com.example.dogeville.model.Pet;
import com.example.dogeville.model.TokenResponse;
import com.example.dogeville.model.Wingman;
import com.example.dogeville.model.WingmanAuth;
import com.example.dogeville.repository.wingmanRepository;
import com.example.dogeville.security.JwtUtil;
import com.google.gson.Gson;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class wingmanService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final wingmanRepository repo;
    private final JavaMailSender mailSender;
    Gson gson = new Gson();

    public wingmanService(wingmanRepository repo, JavaMailSender mailSender,
                          JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.repo = repo;
        this.mailSender = mailSender;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public boolean doesMailExist(String mail) {
        if (repo.findByWmmail(mail) == null) {
            return false;
        }
        return true;
    }


    public String loginVerification(String mail) throws ParseException {
        Wingman old;
        old = getWingmanByMail(mail);
        if (old != null) {
            if (!old.isEnabled()) {
                String json = gson.toJson("noverification");
                return json;
            }
        }
        return null;
    }

    public Wingman getWingmanById(UUID id){
        return repo.findWingmanByWmid(id);
    }

    public Wingman getWingmanByMail(String mail) {
        Wingman wm = repo.findByWmmail(mail);
        System.out.println("retrieved as" + wm.getWmbirth());
        return wm;
    }

    public void addWingman(Wingman wm) {
        repo.saveAndFlush(wm);
    }

    public void updateWingman(Wingman wm) {
        repo.saveAndFlush(wm);
    }

    public Wingman editWingman(String mail, String name, String surname, String gender, String birth,
                               String edu, String job, String country, String state, String county) throws ParseException {
        Wingman old = getWingmanByMail(mail);
        old.setWmname(name);
        old.setWmsurname(surname);
        old.setWmgender(gender);
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(birth);
        old.setWmbirth(date);
        if (edu != null && !edu.equals("null")) {
            old.setWmedu(edu);
        }
        if (job != null && !job.equals("null")) {
            old.setWmjob(job);
        }
        old.setWmcountry(country);
        old.setWmstate(state);
        old.setWmcounty(county);
        repo.saveAndFlush(old);
        return old;
    }

    public ResponseEntity<TokenResponse> loginWingman(WingmanAuth authRequest) throws AuthenticationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getMail(), authRequest.getPassword()));
        final Wingman wm = repo.findByWmmail(authRequest.getMail());
        final String token = jwtUtil.generateToken(wm);
        return ResponseEntity.ok(new TokenResponse(wm.getWmmail(), token));
    }

    public void registerWingman(Wingman wm) throws UnsupportedEncodingException, MessagingException {
        encodePassword(wm);
        wm.setWmenabled(false);
        String randomCode = RandomString.make(64);
        wm.setWmverification(randomCode);
        addWingman(wm);
        sendVerificationMail(wm, "http://localhost:4200/");
    }


    public void sendVerificationMail(Wingman wm, String url) throws UnsupportedEncodingException, MessagingException {
        String subject = "Dogeville Account Verification";
        String sender = "Dogeville Project";
        String content = "<p>Welcome to Dogeville family " + wm.getWmname() + " " + wm.getWmsurname() + "!</p>";
        content += "<p>Please click the link below to officially become a wingman:</p>";
        String verify = url + "wingman/verify?code=" + wm.getWmverification();
        content += "<a href=\"" + verify + "\" href=\\\"\" + verify + \"\\\">VERIFY</a>";
        content += "<p>Thank you<br>The Dogeville Project</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("dogevilleproject@gmail.com", sender);
        helper.setTo(wm.getWmmail());
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    public boolean resetPassword(Wingman wm) throws UnsupportedEncodingException, MessagingException {
        String randomCode = RandomString.make(64);
        wm.setWmpasswordreset(randomCode);
        String url = "http://localhost:4200/";
        String subject = "Dogeville Reset Password";
        String sender = "Dogeville Project";
        String content = "<p>Hello " + wm.getWmname() + " " + wm.getWmsurname() + "!</p>";
        content += "<p>Please click the link below to reset your password:</p>";
        String reset = url + "wingman/reset?code=" + wm.getWmpasswordreset();
        content += "<a href=\"" + reset + "\" href=\\\"\" + reset + \"\\\">RESET PASSWORD</a>";
        content += "<p>Sincerely,<br>The Dogeville Project</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("dogevilleproject@gmail.com", sender);
        helper.setTo(wm.getWmmail());
        helper.setSubject(subject);
        helper.setText(content, true);
        repo.saveAndFlush(wm);
        mailSender.send(message);
        return true;
    }

    public boolean verify(String vercode) {
        Wingman wm = repo.findByWmverification(vercode);

        if (wm == null || wm.isEnabled()) {
            return false;
        } else {
            wm.setWmenabled(true);
            updateWingman(wm);
            return true;
        }
    }

    public boolean updateMail(String mail, String newmail, String pw) {
        Wingman old = getWingmanByMail(mail);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(pw, old.getWmpw())) {
            old.setWmmail(newmail);
            repo.saveAndFlush(old);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean updatePassword(String mail, String newpassword, String password) {
        Wingman old = getWingmanByMail(mail);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(password, old.getWmpw())) {
            old.setWmpw(newpassword);
            encodePassword(old);
            repo.saveAndFlush(old);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean updatePasswordWithCode(String code, String newpassword) {
        Wingman old = repo.findByWmpasswordreset(code);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(newpassword, old.getWmpw())) {
            old.setWmpw(newpassword);
            encodePassword(old);
            repo.saveAndFlush(old);
            return true;
        }
        else{
            return false;
        }
    }

    public void encodePassword(Wingman wm) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(wm.getWmpw());
        wm.setWmpw(encodedPassword);
    }

    public boolean uploadImage(MultipartFile file) throws IOException {
        byte[] image = file.getBytes();

        Wingman old = getWingmanByMail(file.getOriginalFilename());
        old.setWmphoto(image);
        repo.saveAndFlush(old);
        return true;
    }

    public String downloadImage(String mail) {
        Wingman old = getWingmanByMail(mail);
        if (old.getWmphoto() == null) {
            return null;
        } else {
            byte[] retrievedImage = old.getWmphoto();
            String json = gson.toJson(retrievedImage);
            return json;
        }
    }
}
