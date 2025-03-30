package com.example.dtos;

import com.example.models.Endereco;

public class EnderecoResponseDTO {
    private String logradouro;    
    private String numero;    
    private String cidade;    
    private String estado;
    private String cep;

    public EnderecoResponseDTO(Endereco endereco) {
        this.logradouro = endereco.getLogradouro();
        this.numero = endereco.getNumero();
        this.cidade = endereco.getCidade();
        this.estado = endereco.getEstado();
        this.cep = endereco.getCep();
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getCep() {
        return cep;
    }

    
}

