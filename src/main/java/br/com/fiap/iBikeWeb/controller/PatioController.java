package br.com.fiap.iBikeWeb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.iBikeWeb.model.Patio;
import br.com.fiap.iBikeWeb.service.PatioService;

@Controller
@RequestMapping("/patios")
public class PatioController {

    private final PatioService patioService;

    public PatioController(PatioService patioService) {
        this.patioService = patioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("patios", patioService.listarAtivos());
        return "patios/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Patio patio) {
        patioService.salvar(patio);
        return "redirect:/patios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("patio", patioService.buscarPorId(id));
        return "patios/form";
    }

    @PostMapping("/editar")
    public String salvarEdicao(@ModelAttribute Patio patio) {
        patioService.salvar(patio);
        return "redirect:/patios";
    }

    @PostMapping("/desativar/{id}")
    public String desativar(@PathVariable Long id) {
        patioService.desativar(id);
        return "redirect:/patios";
    }
}
