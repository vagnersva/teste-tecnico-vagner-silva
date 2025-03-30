package com.example.dtos;

import com.example.enums.MotivoReemissao;

import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class ReemissaoCartaoRequestDTO {
    @NotNull
    private String numero;

    @Enumerated(EnumType.STRING)
    private MotivoReemissao motivo;
    
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public MotivoReemissao getMotivo() {
        return motivo;
    }
    public void setMotivo(MotivoReemissao motivoReemissao) {
        this.motivo = motivoReemissao;
    }

    

}
