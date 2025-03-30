package com.example.models;

import java.time.LocalDateTime;

import com.example.enums.MotivoReemissao;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "reemissao_cartao")
public class ReemissaoCartao extends PanacheEntityBase {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartao_id", nullable = false)
    private Cartao cartao; 

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo", nullable = false, columnDefinition = "ENUM('PERDA','ROUBO') DEFAULT 'PERDA'")
    private MotivoReemissao motivo;

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public MotivoReemissao getMotivo() {
        return motivo;
    }

    public void setMotivo(MotivoReemissao motivo) {
        this.motivo = motivo;
    }

    public ReemissaoCartao(Cartao cartao, MotivoReemissao motivo) {
        this.cartao = cartao;
        this.motivo = motivo;
    }    

    public ReemissaoCartao() {}        
}