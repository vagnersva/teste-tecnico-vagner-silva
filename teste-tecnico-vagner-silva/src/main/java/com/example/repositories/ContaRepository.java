package com.example.repositories;

import java.util.Optional;

import com.example.models.Conta;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContaRepository implements PanacheRepository<Conta> {
    public Optional<Conta> buscarPorCpf(String cpf) {
        return find("cpf", cpf).firstResultOptional();
    }
}