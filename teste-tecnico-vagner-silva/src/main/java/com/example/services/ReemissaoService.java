package com.example.services;

import com.example.dtos.ReemissaoCartaoRequestDTO;
import com.example.models.Cartao;
import com.example.models.ReemissaoCartao;
import com.example.repositories.ReemissaoCartaoRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ReemissaoService {
    @Inject
    ReemissaoCartaoRepository repository;

    @Transactional
    public void salvarReemissaoCartao(Cartao cartaoBloqueado, ReemissaoCartaoRequestDTO dto) {        
        ReemissaoCartao reemissao = new ReemissaoCartao(cartaoBloqueado, dto.getMotivo());
        repository.persist(reemissao);
    }
}