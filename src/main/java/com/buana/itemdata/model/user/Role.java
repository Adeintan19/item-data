package com.buana.itemdata.model.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    //@Type(type = ("uuid-char"))
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;


}