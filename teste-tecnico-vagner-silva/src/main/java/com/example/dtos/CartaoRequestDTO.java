package com.example.dtos;

import com.example.enums.TipoCartao;

import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class CartaoRequestDTO {
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoCartao tipo; 

    @NotNull
    private Long contaId;

    public CartaoRequestDTO() {}

    public CartaoRequestDTO(TipoCartao tipo) {
        this.tipo = tipo;
    }

    public CartaoRequestDTO(TipoCartao tipo, Long contaId) {
        this.tipo = tipo;
        this.contaId = contaId;
    }

    public TipoCartao getTipo() {
        return tipo;
    }

    public void setTipo(TipoCartao tipo) {
        this.tipo = tipo;
    }

    public Long getContaId() {
        return contaId;
    }

    public void setContaId(Long contaId) {
        this.contaId = contaId;
    }
}
