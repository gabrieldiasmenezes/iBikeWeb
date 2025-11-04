package br.com.fiap.iBikeWeb.service;

import org.springframework.stereotype.Service;
import br.com.fiap.iBikeWeb.model.Patio;
import br.com.fiap.iBikeWeb.repository.PatioRepository;
import br.com.fiap.iBikeWeb.repository.MotoRepository;
import br.com.fiap.iBikeWeb.components.StatusPatio;

import java.util.List;

@Service
public class PatioService {

    private final PatioRepository repository;
    private final MotoRepository motoRepository;

    public PatioService(PatioRepository repository, MotoRepository motoRepository) {
        this.repository = repository;
        this.motoRepository = motoRepository;
    }

    public List<Patio> listarAtivos() {
        List<Patio> patios = repository.findByStatus(StatusPatio.ATIVO);
        patios.forEach(p -> p.setMotosPresentes(motoRepository.countByPatio(p)));
        return patios;
    }

    public Patio buscarPorId(Long id) {
        Patio patio = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pátio não encontrado"));
        patio.setMotosPresentes(motoRepository.countByPatio(patio));
        return patio;
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
