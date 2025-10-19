package br.com.fiap.iBikeWeb.controller;

import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.iBikeWeb.model.Administrador;
import br.com.fiap.iBikeWeb.model.Moto;
import br.com.fiap.iBikeWeb.service.MotoService;
import br.com.fiap.iBikeWeb.service.PatioService;
import br.com.fiap.iBikeWeb.components.StatusMoto;

@Controller
@RequestMapping("/motos")
public class MotoController {

    private final MotoService motoService;

    private final PatioService patioService;

    public MotoController(MotoService motoService,PatioService patioService) {
        this.motoService = motoService;
        this.patioService=patioService;
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
    public String novo(Model model) {
        model.addAttribute("moto", new Moto());
        model.addAttribute("statusMoto", StatusMoto.values());
        model.addAttribute("patios", patioService.listarAtivos());
        return "motos/form-add";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Moto moto) {
        if (moto.getStatus() == null) {
            moto.setStatus(StatusMoto.NO_PATIO); // status automático
        }
        if (moto.getDataUltimoCheck() == null) {
            moto.setDataUltimoCheck(LocalDate.now()); // data automática
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
