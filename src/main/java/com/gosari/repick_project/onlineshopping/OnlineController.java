package com.gosari.repick_project.onlineshopping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor // final 로 선언된 클래스를 자동으로 생성합니다.
@Controller
public class OnlineController {

    private final NaverShopSearch naverShopSearch;

    @GetMapping("/onlineshop/list")
    public String getItems(Model model){
        // ? 뒤에 오는 것을 쓰고 싶다면 @RequestParam
        String resultString = naverShopSearch.search();

        List<OnlineshopItemDto> itemDtos = naverShopSearch.fromJSONtoItems(resultString);
        model.addAttribute("itemDtos", itemDtos);

        return "onlineshop_list";
    }
}