package com.fiap.ponabri.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {

    @GetMapping("/lista-abrigos")
    public String abrigos() {
        return "abrigos";
    }

    // Removed conflicting mapping to resolve ambiguous mapping error
    // @GetMapping("/reservas")
    // public String reservas() {
    //     return "reservas";
    // }

    @GetMapping("/usuariosPage")
    public String usuariosPage() {
        return "usuarios";
    }
}
