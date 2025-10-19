package br.com.fiap.iBikeWeb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.iBikeWeb.model.Administrador;


public interface AdministradorRepository extends JpaRepository<Administrador, String> {
    Optional<Administrador> findByEmail(String email);
}
