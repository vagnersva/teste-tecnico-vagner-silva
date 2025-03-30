package com.example.models;

import java.time.LocalDateTime;

import com.example.enums.DeliveryStatus;
import com.example.enums.StatusCartao;
import com.example.enums.TipoCartao;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "cartao")
public class Cartao extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, length = 16)
    private String numero;

    @Column(name = "titular", nullable = false, length = 100)
    private String titular;

    @Column(name = "cvv", nullable = false, length = 3)
    private String cvv;

    @Column(name = "data_validade", nullable = false, columnDefinition = "CHAR(5)") 
    private String dataValidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, columnDefinition = "ENUM('FISICO','VIRTUAL') DEFAULT 'FISICO'")
    private TipoCartao tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('ATIVO','BLOQUEADO','INATIVO') DEFAULT 'INATIVO'")
    private StatusCartao status;

    @Column(name = "data_criacao", nullable = false, insertable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao", nullable = false, insertable = false, updatable = false)
    private LocalDateTime dataAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id", nullable = false)
    private Conta conta;

    @Column(name = "tracking_id")
    private String trackingId; 

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus; 

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate; 

    @Column(name = "delivery_return_reason")
    private String deliveryReturnReason;

    @OneToOne(mappedBy = "cartao", cascade = CascadeType.ALL, orphanRemoval = true)
    private Endereco deliveryAddress; 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(String dataValidade) {
        this.dataValidade = dataValidade;
    }

    public TipoCartao getTipo() {
        return tipo;
    }

    public void setTipo(TipoCartao tipo) {
        this.tipo = tipo;
    }

    public StatusCartao getStatus() {
        return status;
    }

    public void setStatus(StatusCartao status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryReturnReason() {
        return deliveryReturnReason;
    }

    public void setDeliveryReturnReason(String deliveryReturnReason) {
        this.deliveryReturnReason = deliveryReturnReason;
    }

    public Endereco getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Endereco deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }    
}