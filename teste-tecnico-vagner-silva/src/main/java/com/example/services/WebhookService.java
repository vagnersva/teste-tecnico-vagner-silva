package com.example.services;

import java.time.LocalDateTime;
import java.util.List;

import com.example.dtos.EnderecoRequestDTO;
import com.example.dtos.RotacaoCvvWebhookDTO;
import com.example.dtos.TransportadoraWebhookDTO;
import com.example.enums.DeliveryStatus;
import com.example.enums.TipoCartao;
import com.example.models.Cartao;
import com.example.models.Conta;
import com.example.models.Endereco;
import com.example.repositories.ReemissaoCartaoRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class WebhookService {
    @Inject
    ReemissaoCartaoRepository repository;

    @Inject
    CartaoService cartaoService;

    @Inject
    ContaService contaService;

    @Transactional
    public void processarAtualizacaoEntrega(TransportadoraWebhookDTO dto) {
        Cartao cartao = cartaoService.buscarPorNumeroCartao(dto.getNumeroCartao())
            .orElseThrow(() -> new NotFoundException("Cartão não encontrado."));

        
        if (cartao.getTipo() != TipoCartao.FISICO) {
            throw new IllegalArgumentException("Apenas cartões físicos podem ter sua entrega confirmada.");
        }
        
        if (dto.getDeliveryStatus() == DeliveryStatus.ENTREGUE) {
            atualizarDadosEntrega(cartao, dto);
        }

    }

    private void atualizarDadosEntrega(Cartao cartao, TransportadoraWebhookDTO dto) {
        EnderecoRequestDTO enderecoEntregaDTO = dto.getDeliveryAddress();
        Endereco enderecoEntrega = new Endereco(
            cartao.getConta(), 
            enderecoEntregaDTO.getLogradouro(),
            enderecoEntregaDTO.getNumero(),
            enderecoEntregaDTO.getCidade(),
            enderecoEntregaDTO.getEstado(),
            enderecoEntregaDTO.getCep());

        cartao.setDeliveryStatus(dto.getDeliveryStatus());
        cartao.setDeliveryDate(dto.getDeliveryDate());
        cartao.setDeliveryReturnReason(dto.getDeliveryReturnReason());
        cartao.setDeliveryAddress(enderecoEntrega);

        System.err.println(cartao.getDeliveryStatus());
        System.err.println(cartao.getNumero());
        cartaoService.atualizarDadosCartao(cartao);
    }

    @Transactional
    public void processarRotacaoCvv(RotacaoCvvWebhookDTO dto) {
        
        System.err.println(dto.getCardId());
        
        Cartao cartao = cartaoService.buscarPorCartaoId(dto.getCardId())
            .orElseThrow(() -> new NotFoundException("Cartão não encontrado"));

        validarExpiracaoCvv(dto.getExpirationDate());
        validarDadosCartao(dto);
        
        
        cartao.setCvv(String.format("%03d", dto.getNextCvv())); 
        cartao.setDataAtualizacao(LocalDateTime.now());
        
        cartaoService.atualizarDadosCartao(cartao);
    }

    private void validarDadosCartao(RotacaoCvvWebhookDTO dto) {
        Conta conta = contaService.buscarPorContaId(dto.getAccountId())
            .orElseThrow(() -> new NotFoundException("Conta não encontrada."));

        List<Long> numerosCartoes = conta.getCartoes().stream().map(Cartao::getId).toList();

        if (!numerosCartoes.contains(dto.getCardId())) {
            throw new NotFoundException("Cartão não encontrado.");
        }
    }

    private void validarExpiracaoCvv(LocalDateTime expirationDate) {
        if (expirationDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Data de expiração do CVV inválida.");
        }
    }
}