package com.udacity.jwdnd.course1.cloudstorage.entities;

import lombok.Data;

@Data
public class User {
    private Integer userid;
    private String username;
    private String salt;
    private String password;
    private String firstname;
    private String lastname;
}
