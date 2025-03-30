package com.example.repositories;

import com.example.models.ReemissaoCartao;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReemissaoCartaoRepository implements PanacheRepository<ReemissaoCartao> {
    
}