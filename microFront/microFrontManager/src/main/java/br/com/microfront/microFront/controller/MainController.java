package br.com.microfront.microFront.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;  

@Controller
public class MainController {

    @GetMapping("/")
    public String oauthUserInfo(Model model) {
        return  "home.html";
    }  
}