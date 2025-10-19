package br.com.fiap.iBikeWeb.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.iBikeWeb.model.Administrador;
import br.com.fiap.iBikeWeb.repository.PatioRepository;
import br.com.fiap.iBikeWeb.service.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    private final PatioRepository patioRepository;

    public UsuarioController(UsuarioService usuarioService,PatioRepository patioRepository) {
        this.usuarioService = usuarioService;
        this.patioRepository=patioRepository;
    }

    @GetMapping
    public String perfil(@AuthenticationPrincipal Administrador adm, Model model) {
        model.addAttribute("usuario", adm);
        model.addAttribute("patios", patioRepository.findAll()); // lista de todos os pátios para todos
        return "usuario/perfil";
    }

    @PostMapping("/editar")
    public String editar(@AuthenticationPrincipal Administrador adm,
                         @RequestParam String nome,
                         @RequestParam String email,
                         @RequestParam(required = false) Long patioId) {
        usuarioService.atualizarDados(adm, nome, email,patioId);
        return "redirect:/usuario?editado";
    }
    @PostMapping("/desativar")
    public String desativar(@AuthenticationPrincipal Administrador adm) {
        usuarioService.desativarConta(adm);
         return "redirect:/login?desativado";
    }
}
