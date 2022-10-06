package com.gosari.repick_project.user;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String nickname;

    private String password;

    @Column(unique = true)
    private String email;

    private String role;

    @PrePersist
    public void setting(){
        this.role = "ROLE_USER";
    }

    private String profileImage;

    private String ImageName;
}