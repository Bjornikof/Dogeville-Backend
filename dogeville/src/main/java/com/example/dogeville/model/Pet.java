package com.example.dogeville.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "pet")
public class Pet implements Serializable {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Type(type = "pg-uuid")
    @Column(name = "pid", unique = true)
    private UUID pid;

    @ManyToOne
    @JoinColumn(name = "wmid", nullable = false)
    private Wingman wmid;


    @Pattern(regexp = "[^0-9]*", message = "name can't include numbers.")
    @NotEmpty(message = "provide a name.")
    @NotNull(message = "provide a name.")
    @Column(name = "pname", nullable = false)
    private String pname;

    @NotEmpty(message = "provide a breed.")
    @NotNull(message = "provide a breed.")
    @Column(name = "pbreed", columnDefinition = "text", nullable = false)
    private String pbreed;

    @NotEmpty(message = "provide a gender.")
    @NotNull(message = "provide a gender.")
    @Column(name = "pgender", columnDefinition = "text", nullable = false)
    private String pgender;

    @NotNull(message = "provide a birthday.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "pbirth", nullable = false)
    private Date pbirth;

    @NotNull(message = "provide a breeding status.")
    @Column(name = "pisbred", nullable = false)
    private Boolean pisbred;

    @Column(name = "pcolor", columnDefinition = "text")
    private String pcolor;

    @Column(name = "ptrick", columnDefinition = "text")
    private String ptrick;

    @Column(name = "pfav", columnDefinition = "text")
    private String pfav;

    @Column(name = "pdislike", columnDefinition = "text")
    private String pdislike;

    @Column(name = "pletter", columnDefinition = "text")
    private String pletter;

    @Column(name = "pphoto")
    private byte[] pphoto;

    @NotNull(message = "provide a pet type.")
    @Column(name = "ptype", columnDefinition = "text", nullable = false)
    private String ptype;

}
