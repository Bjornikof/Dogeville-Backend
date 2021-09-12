package com.example.dogeville.repository;

import com.example.dogeville.model.Pet;
import com.example.dogeville.model.Wingman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Repository
public interface wingmanRepository extends JpaRepository<Wingman, UUID> {
    Wingman findByWmmail(@Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$", message = "not a valid email address.") @NotEmpty(message = "provide an email address.") @NotNull(message = "provide an email address.") String wmmail);

    Wingman findByWmverification(String wmverification);
    Wingman findByWmpasswordreset(String wmver);
    Wingman findWingmanByWmid(UUID id);

}
