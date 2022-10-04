package com.gosari.repick_project.offlineshopping;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class OfflineForm {

    @NotEmpty(message = "매장명은 필수항목입니다.")
    @Size(max=200)
    private String shopName;

    @NotEmpty(message = "매장연락처는 필수항목입니다.")
    @Size(max=200)
    private String shopNumber;

    @NotEmpty(message = "운영시간은 필수항목입니다.")
    @Size(max=200)
    private String shopTime;

    @NotEmpty(message = "매장주소는 필수항목입니다.")
    @Size(max=200)
    private String ShopAddress;

    private String shopContents;

}
