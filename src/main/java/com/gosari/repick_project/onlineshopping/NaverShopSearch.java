package com.gosari.repick_project.onlineshopping;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@Component // @RequiredArgsConstructor 와 함께 사용할 경우 스프링이 자동으로 생성
public class NaverShopSearch {


    public String search() {
        String upcyclingshop = "업사이클링";
        // https://openapi.naver.com/v1/search/shop.xml?query=%EC%A3%BC%EC%8B%9D&display=10&start=1&sort=sim
        URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com").path("/v1/search/shop")
                .queryParam("query", upcyclingshop).queryParam("display", 40).queryParam("start", 1).queryParam("sort", "sim")
                .encode().build().toUri();

        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> requestEntity = RequestEntity.get(uri).header("X-Naver-Client-Id", "Qdlklvrsmjdx6WV2d4ct")
                .header("X-Naver-Client-Secret", "WluCbERIMc").build();
        ResponseEntity<String> result = restTemplate.exchange(requestEntity, String.class);


        String response = result.getBody();

        return response;
    }

    public static void main(String[] args) {
        NaverShopSearch naverShopSearch = new NaverShopSearch();
        naverShopSearch.search();
    }

    public List<OnlineshopItemDto> fromJSONtoItems(String result)  {
        // 문자열 정보를 JSONObject로 바꾸기
        JSONObject rjson = new JSONObject(result);
        System.out.println(rjson);
        // JSONObject에서 items 배열 꺼내기
        // JSON 배열이기 때문에 보통 배열이랑 다르게 활용해야한다.
        JSONArray items = rjson.getJSONArray("items");
        List<OnlineshopItemDto> itemDtoList = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject itemJson = (JSONObject) items.get(i);
            OnlineshopItemDto itemDto = new OnlineshopItemDto(itemJson);
            itemDtoList.add(itemDto);
            // item 중에서 필요한 항목만 꺼내기
            //String title = itemJson.getString("title");
            //String image = itemJson.getString("image");
            //String link = itemJson.getString("link");
            //int lprice = itemJson.getInt("lprice");
        }
        return itemDtoList;
    }
}