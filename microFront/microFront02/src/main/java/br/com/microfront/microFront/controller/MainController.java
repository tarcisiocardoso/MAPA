package br.com.microfront.microFront.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;  

@Controller
public class MainController {

    @CrossOrigin(origins = "*")
    @GetMapping("/")
    public String oauthUserInfo(Model model) {
        System.out.println(">>>>>>>>>>>>>CLIENTE 01 SENDO CHAMADO<<<<<<<<");
        return  "home.html";
    }  
}