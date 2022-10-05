package com.gosari.repick_project.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


@Getter
@Setter
public class UserModifyForm {

    @NotEmpty(message = "수정할 닉네임을 입력해주세요.")
    private String nickname;

    @NotEmpty(message = "수정할 이메일을 입력해주세요.")
    @Email
    private String email;

    private String profileImage;

    private String ImageName;
}
