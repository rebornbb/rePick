package com.gosari.repick_project.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserUpdateForm {

    @NotEmpty(message = "닉네임은 필수항목입니다.")
    private String nickname;

    private String profileImage;

    private String ImageName;

}
