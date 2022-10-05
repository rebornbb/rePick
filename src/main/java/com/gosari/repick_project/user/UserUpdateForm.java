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

//    @NotEmpty(message = "비밀번호는 필수항목입니다.")
//    private String password1;
//
//    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
//    private String password2;

}
