package br.com.fiap.iBikeWeb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AcessoNegadoController {

    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "acesso-negado"; // nome do template HTML (sem .html)
    }
}
