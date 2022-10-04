package com.gosari.repick_project.onlineshopping;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Getter
@Setter
public class OnlineshopItemDto {
    private String title;
    private String link;
    private String image;
    private int lprice;

//    private String mallName;
//    private String brand;
//    private String category1;
//    private String category2;
//    private String category3;
//    private String category4;

    public OnlineshopItemDto(JSONObject itemJson) {
        this.title = itemJson.getString("title");
        this.link = itemJson.getString("link");
        this.image = itemJson.getString("image");
        this.lprice = itemJson.getInt("lprice");
    }
}
