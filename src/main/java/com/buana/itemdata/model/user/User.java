package com.buana.itemdata.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import static javax.persistence.FetchType.EAGER;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {


    @Id
    @Type(type = ("uuid-char"))
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    @NotNull
    private String username;
    @JsonIgnore
    private String password;
    @NotNull
    private String email;
    private int failedAttempt;
    private boolean enabled = true;
    private boolean accountNonLocked = true;
    private Date lockTime;
    private Date failedTime;
    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();


    @PrePersist
    public void setCreatedAtBeforeInsert() {
        this.id = UUID.randomUUID();
    }


}