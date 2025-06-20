package com.fiap.ponabri.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2LoginController {

    @GetMapping("/oauth2-login")
    public String oauth2Login() {
        return "oauth2-login";
    }
}
