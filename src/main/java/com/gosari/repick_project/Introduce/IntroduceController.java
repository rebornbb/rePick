package com.gosari.repick_project.Introduce;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IntroduceController {
    @RequestMapping("/introduce")
    public String introduce(){
        return "introduce";
    }
}
