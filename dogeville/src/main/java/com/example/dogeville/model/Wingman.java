package com.example.dogeville.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ToString(onlyExplicitlyIncluded = true, callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "wingman")
public class Wingman implements Serializable {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Type(type = "pg-uuid")
    @Column(name = "wmid", unique = true)
    private UUID wmid;

    @Pattern(regexp = "[^0-9]*", message = "name can't include numbers.")
    @NotEmpty(message = "provide a name.")
    @NotNull(message = "provide a name.")
    @Column(name = "wmname", nullable = false)
    private String wmname;

    @Pattern(regexp = "[^0-9]*", message = "surname can't include numbers.")
    @NotEmpty(message = "provide a surname.")
    @NotNull(message = "provide a surname.")
    @Column(name = "wmsurname", nullable = false)
    private String wmsurname;

    @NotEmpty(message = "provide a gender.")
    @NotNull(message = "provide a gender.")
    @Column(name = "wmgender", columnDefinition = "text", nullable = false)
    private String wmgender;


    @NotNull(message = "provide a birthday.")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "wmbirth", nullable = false)
    private Date wmbirth;

    @Pattern(regexp = "[^0-9]*", message = "education can't include numbers.")
    @Column(name = "wmedu", columnDefinition = "text")
    private String wmedu;

    @Pattern(regexp = "[^0-9]*", message = "job can't include numbers.")
    @Column(name = "wmjob")
    private String wmjob;

    @NotEmpty(message = "provide a country.")
    @NotNull(message = "provide a country.")
    @Column(name = "wmcountry", columnDefinition = "text", nullable = false)
    private String wmcountry;

    @NotEmpty(message = "provide a province/state")
    @NotNull(message = "provide a province/state.")
    @Column(name = "wmstate", columnDefinition = "text", nullable = false)
    private String wmstate;

    @NotEmpty(message = "provide a county.")
    @NotNull(message = "provide a county.")
    @Column(name = "wmcounty", columnDefinition = "text", nullable = false)
    private String wmcounty;

    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$", message = "not a valid email address.")
    @NotEmpty(message = "provide an email address.")
    @NotNull(message = "provide an email address.")
    @Column(name = "wmmail", nullable = false)
    private String wmmail;

    @NotEmpty(message = "provide a password.")
    @NotNull(message = "provide a password.")
    @Column(name = "wmpw", nullable = false)
    private String wmpw;

    @Type(type="org.hibernate.type.BinaryType")
    @Column(name = "wmphoto", columnDefinition = "text", length = 1000)
    private byte[] wmphoto;

    @Column(name = "wmverification", updatable = false)
    private String wmverification;

    @Column(name = "wmenabled")
    private boolean wmenabled;

    @Column(name = "wmpasswordreset")
    private String wmpasswordreset;

    @JsonBackReference
    @OneToMany(mappedBy = "wmid")
    Set<Pet> pets = new HashSet();

    public boolean isEnabled(){
        if (wmenabled){
            return true;
        }else{
            return false;
        }
    }

}
