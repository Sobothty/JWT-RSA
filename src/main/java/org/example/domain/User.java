package org.example.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class User {
    private String uuid;
    private String email;
    private String password;
    private String role;
}
