package br.com.fiap.iBikeWeb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.iBikeWeb.components.StatusPatio;
import br.com.fiap.iBikeWeb.model.Patio;


public interface PatioRepository extends JpaRepository<Patio, Long> {
    List<Patio> findByStatus(StatusPatio status);
}
