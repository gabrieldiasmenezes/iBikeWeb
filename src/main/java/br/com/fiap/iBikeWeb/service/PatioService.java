package br.com.fiap.iBikeWeb.service;

import org.springframework.stereotype.Service;
import br.com.fiap.iBikeWeb.model.Patio;
import br.com.fiap.iBikeWeb.repository.PatioRepository;
import br.com.fiap.iBikeWeb.components.StatusPatio;

import java.util.List;

@Service
public class PatioService {

    private final PatioRepository repository;

    public PatioService(PatioRepository repository) {
        this.repository = repository;
    }

    public List<Patio> listarAtivos() {
        return repository.findByStatus(StatusPatio.ATIVO);
    }

    public Patio buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pátio não encontrado"));
    }

    public void salvar(Patio patio) {
        patio.setStatus(StatusPatio.ATIVO);
        repository.save(patio);
    }

    public void desativar(Long id) {
        Patio patio = buscarPorId(id);
        patio.setStatus(StatusPatio.INATIVO); 
        repository.save(patio);
    }
}
