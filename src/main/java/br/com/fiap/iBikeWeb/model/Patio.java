package br.com.fiap.iBikeWeb.model;

import java.util.List;
import br.com.fiap.iBikeWeb.components.StatusPatio;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patio")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patio {

    @Id
    @Column(name = "id_patio")
    private Long id;

    @Column(name = "nm_patio", nullable = false)
    private String nome;

    @Column(nullable = false)
    private int capacidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPatio status;

    @OneToMany(mappedBy = "patio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Administrador> administradores;

    @OneToMany(mappedBy = "patio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Moto> motos;

    // Campo não persistido no banco, usado só para exibir no front
    @Transient
    private int motosPresentes;
}
