package br.com.fiap.iBikeWeb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import br.com.fiap.iBikeWeb.components.StatusMoto;

@Entity
@Table(name = "moto")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Moto {

    @Id
    private String placa;

    private String modelo;

    @Enumerated(EnumType.STRING)
    private StatusMoto status;

    @Column(name = "km_atual")
    private double kmAtual;

    @Column(name = "data_ultimo_check")
    private LocalDate dataUltimoCheck;

    @ManyToOne
    @JoinColumn(name = "id_patio")
    private Patio patio;
}
