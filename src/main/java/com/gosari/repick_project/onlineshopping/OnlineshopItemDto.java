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

    public OnlineshopItemDto(JSONObject itemJson) {
        this.title = itemJson.getString("title");
        this.link = itemJson.getString("link");
        this.image = itemJson.getString("image");
        this.lprice = itemJson.getInt("lprice");
    }
}
