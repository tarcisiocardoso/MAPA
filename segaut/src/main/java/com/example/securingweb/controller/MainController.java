package com.example.securingweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    
    @GetMapping("/greeting")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}

	@GetMapping("/perform_logout")
	public String logout(Model model) {
		return "sair";
	}

	@GetMapping("/lista")
	public String lista(Model model) {
		return "lista";
	}

	@GetMapping("/alterarSenha")
	public String alterarSenha(Model model) {
		return "alterarSenha";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}
}