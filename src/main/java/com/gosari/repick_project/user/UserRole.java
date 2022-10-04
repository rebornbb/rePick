package com.gosari.repick_project.user;

import lombok.Getter;

@Getter /*enum 열거자료형 , 상수자료형이므로 Getter만 사용가능*/
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
