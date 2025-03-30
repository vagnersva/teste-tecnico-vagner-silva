package com.example.repositories;

import java.util.List;
import java.util.Optional;

import com.example.models.Cartao;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CartaoRepository implements PanacheRepository<Cartao> {
    public Optional<Cartao> buscarPorContaId(Integer contaId) {
        return find("contaId", contaId).firstResultOptional();
    }

    @SuppressWarnings("unchecked")
    public List<Cartao> buscarCartoes(Integer contaId) {
        return (List<Cartao>) findAll();
    }

    public Optional<Cartao> buscarCartaoPorNumeroCartao(String numeroCartao) {
        return find("numero", numeroCartao).firstResultOptional();
    }

    public Optional<Cartao> buscarCartaoPorTrackingId(String trackingId) {
        return find("trackingId", trackingId).firstResultOptional();
    }
}