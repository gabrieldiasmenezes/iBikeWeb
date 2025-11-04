package br.com.fiap.iBikeWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fiap.iBikeWeb.model.Moto;
import br.com.fiap.iBikeWeb.model.Patio;
import br.com.fiap.iBikeWeb.components.StatusMoto;
import java.util.List;

public interface MotoRepository extends JpaRepository<Moto, String> {
    List<Moto> findByStatus(StatusMoto status);
    List<Moto> findByPatioId(Long idPatio);
    int countByPatio(Patio patio);
}
