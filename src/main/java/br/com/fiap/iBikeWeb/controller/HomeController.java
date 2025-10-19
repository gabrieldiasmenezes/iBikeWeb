package br.com.fiap.iBikeWeb.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.fiap.iBikeWeb.model.Administrador;
import br.com.fiap.iBikeWeb.components.StatusAdministrador;

import br.com.fiap.iBikeWeb.service.PatioService;

@Controller
public class HomeController {
    private final PatioService patioService;

    public HomeController(PatioService patioService) {
        this.patioService=patioService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {

        if (error != null) {
            model.addAttribute("loginError", "Email ou senha inválidos, ou usuário inativo.");
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "Você saiu com sucesso.");
        }

        return "login"; // página login.html
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal Administrador admin, Model model) {
        // adiciona o nome e status do admin logado
        model.addAttribute("nome", admin.getNome());
        model.addAttribute("status", admin.getStatus());

        if (admin.getStatus() == StatusAdministrador.ADMIN) {
            model.addAttribute("patios", patioService.listarAtivos());
            model.addAttribute("admin", true);
        } else {
            model.addAttribute("patio", admin.getPatio());
            model.addAttribute("admin", false);
        }

        return "home"; // home.html
    }
}
