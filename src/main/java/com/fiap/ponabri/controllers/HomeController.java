package com.fiap.ponabri.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/abrigos")
    public String abrigos() {
        return "abrigos";
    }

    @GetMapping("/reservas")
    public String reservas() {
        return "reservas";
    }
}
