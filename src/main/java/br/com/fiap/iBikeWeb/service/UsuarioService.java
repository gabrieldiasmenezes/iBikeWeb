package br.com.fiap.iBikeWeb.service;

import org.springframework.stereotype.Service;

import br.com.fiap.iBikeWeb.model.Administrador;
import br.com.fiap.iBikeWeb.model.Patio;
import br.com.fiap.iBikeWeb.repository.AdministradorRepository;
import br.com.fiap.iBikeWeb.repository.PatioRepository;
import br.com.fiap.iBikeWeb.components.StatusAdministrador;

@Service
public class UsuarioService {

    private final AdministradorRepository repository;
    private final PatioRepository patioRepository;

    public UsuarioService(AdministradorRepository repository, PatioRepository patioRepository) {
        this.repository = repository;
        this.patioRepository = patioRepository;
    }

    public void atualizarDados(Administrador adm, String nome, String email, Long patioId) {
        adm.setNome(nome);
        adm.setEmail(email);

        if (patioId != null) {
            Patio patio = patioRepository.findById(patioId)
                                .orElseThrow(() -> new RuntimeException("Pátio não encontrado"));
            adm.setPatio(patio);
        }

        repository.save(adm);
    }

    public void desativarConta(Administrador adm) {
        // Em vez de deletar, só alteramos o status para INATIVO
        adm.setStatus(StatusAdministrador.INATIVO);
        repository.save(adm);
    }
}
