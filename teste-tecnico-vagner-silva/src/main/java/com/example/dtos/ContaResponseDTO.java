package com.example.dtos;

import com.example.enums.StatusConta;
import com.example.models.Conta;
import com.example.models.Endereco;

public class ContaResponseDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private StatusConta statusConta;
    private EnderecoResponseDTO endereco;    

    public ContaResponseDTO(Conta conta, Endereco endereco) {
        this.id = conta.getId();
        this.nome = conta.getNome();
        this.cpf = conta.getCpf();
        this.telefone = conta.getTelefone();
        this.email = conta.getEmail();
        this.statusConta = conta.getStatusConta();
        this.endereco = new EnderecoResponseDTO(endereco);
    }

    public ContaResponseDTO(){}

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public StatusConta getStatusConta() {
        return statusConta;
    }

    public EnderecoResponseDTO getEndereco() {
        return endereco;
    }

    
}