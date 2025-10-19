package br.com.fiap.iBikeWeb.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.fiap.iBikeWeb.model.Administrador;
import br.com.fiap.iBikeWeb.repository.AdministradorRepository;
import br.com.fiap.iBikeWeb.components.StatusAdministrador;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdministradorRepository repository;

    public CustomUserDetailsService(AdministradorRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Administrador adm = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (adm.getStatus() == StatusAdministrador.INATIVO) {
            throw new UsernameNotFoundException("Conta inativa");
        }

        return adm;
    }
}
