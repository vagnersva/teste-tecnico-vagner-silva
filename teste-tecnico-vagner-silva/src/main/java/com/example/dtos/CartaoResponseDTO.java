package com.example.dtos;

import java.time.LocalDateTime;

import com.example.models.Cartao;

public class CartaoResponseDTO {
    private String numero;
    private String titular;
    private String dataValidade;
    private String tipo;
    private String status;
    private LocalDateTime dataCriacao;

    public CartaoResponseDTO() {}

    public CartaoResponseDTO(String numero, String titular, String dataValidade, 
                            String tipo, String status, LocalDateTime dataCriacao) {
        this.numero = numero;
        this.titular = titular;
        this.dataValidade = dataValidade;
        this.tipo = tipo;
        this.status = status;
        this.dataCriacao = dataCriacao;
    }

    public static CartaoResponseDTO fromEntity(Cartao cartao) {
        return new CartaoResponseDTO(
            cartao.getNumero(),
            cartao.getTitular(),
            cartao.getDataValidade(),
            cartao.getTipo().toString(),
            cartao.getStatus().toString(),
            cartao.getDataCriacao()
        );
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

    public String getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(String dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    
}
