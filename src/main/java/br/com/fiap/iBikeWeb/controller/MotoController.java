package br.com.fiap.iBikeWeb.controller;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.iBikeWeb.model.Administrador;
import br.com.fiap.iBikeWeb.model.Moto;
import br.com.fiap.iBikeWeb.model.Patio;
import br.com.fiap.iBikeWeb.repository.AdministradorRepository;
import br.com.fiap.iBikeWeb.service.MotoService;
import br.com.fiap.iBikeWeb.service.PatioService;
import br.com.fiap.iBikeWeb.components.StatusMoto;

@Controller
@RequestMapping("/motos")
public class MotoController {

    private final MotoService motoService;
    private final PatioService patioService;
    private final AdministradorRepository administradorRepository;

    public MotoController(MotoService motoService, PatioService patioService, AdministradorRepository administradorRepository) {
        this.motoService = motoService;
        this.patioService = patioService;
        this.administradorRepository = administradorRepository;
    }

    @GetMapping
    public String listar(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Administrador admin = (Administrador) auth.getPrincipal(); // ✅ pega o admin logado

        Long idPatio = admin.getPatio().getId();
        model.addAttribute("motos", motoService.listarPorPatio(idPatio));
        return "motos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model, Principal principal) {
        // Pega o admin logado
        Administrador administrador = administradorRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Administrador não encontrado"));

        model.addAttribute("moto", new Moto());
        model.addAttribute("statusMoto", StatusMoto.values());
        model.addAttribute("patioAtual", administrador.getPatio().getNome());
        return "motos/form-add";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Moto moto, Principal principal, Model model) {
        // Obtém o administrador logado
        Administrador administrador = administradorRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Administrador não encontrado"));

        // Define o pátio do usuário logado
        Patio patioUsuario = administrador.getPatio();
        moto.setPatio(patioUsuario);

        // 🔹 Verifica se já existe uma moto com a mesma placa
        Moto existente = motoService.buscarPorPlaca(moto.getPlaca());
        if (existente != null) {
            model.addAttribute("erro", "Já existe uma moto cadastrada com esta placa.");
            model.addAttribute("moto", moto);
            model.addAttribute("patioAtual", patioUsuario.getNome());
            model.addAttribute("statusMoto", StatusMoto.values());
            return "motos/form-add"; // volta pro formulário
        }

        // Define valores automáticos
        if (moto.getStatus() == null) {
            moto.setStatus(StatusMoto.NO_PATIO);
        }

        if (moto.getDataUltimoCheck() == null) {
            moto.setDataUltimoCheck(LocalDate.now());
        }

        motoService.salvar(moto);
        return "redirect:/motos";
    }

    @GetMapping("/editar/{placa}")
    public String editar(@PathVariable String placa, Model model) {
        model.addAttribute("moto", motoService.buscarPorPlaca(placa));
        model.addAttribute("statusMoto", StatusMoto.values());
        model.addAttribute("patios", patioService.listarAtivos());
        return "motos/form-edit";
    }

    @PostMapping("/excluir/{placa}")
    public String excluir(@PathVariable String placa) {
        motoService.excluir(placa);
        return "redirect:/motos";
    }
}
