package br.com.fiap.iBikeWeb.service;

import org.springframework.stereotype.Service;

import br.com.fiap.iBikeWeb.model.Moto;
import br.com.fiap.iBikeWeb.repository.MotoRepository;
import br.com.fiap.iBikeWeb.components.StatusMoto;

import java.util.List;

@Service
public class MotoService {

    private final MotoRepository repository;

    public MotoService(MotoRepository repository) {
        this.repository = repository;
    }

    public List<Moto> listarPorPatio(Long idPatio) {
        return repository.findByPatioId(idPatio);
    }


    public Moto buscarPorPlaca(String placa) {
        return repository.findById(placa).orElseThrow(() -> new RuntimeException("Moto não encontrada"));
    }

    public boolean existePorPlaca(String placa) {
        return repository.existsById(placa);
    }

    public void salvar(Moto moto) {
        repository.save(moto);
    }

    public void excluir(String placa) {
        repository.deleteById(placa);
    }

    public List<Moto> listarPorStatus(StatusMoto status) {
        return repository.findByStatus(status);
    }
}
