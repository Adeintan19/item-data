package com.buana.itemdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserSaveRequest {
    @NotBlank(message = "cannot be null and empty")
    private String name;
    @NotBlank(message = "cannot be null and empty")
    private String username;
    @NotBlank(message = "cannot be null and empty")
    private String email;
    @NotBlank(message = "cannot be null and empty")
    private String password;
}
