package com.example.services;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import org.junit.jupiter.api.Test;

import com.example.dtos.CartaoRequestDTO;
import com.example.dtos.CartaoResponseDTO;
import com.example.enums.DeliveryStatus;
import com.example.enums.StatusCartao;
import com.example.enums.TipoCartao;
import com.example.models.Cartao;
import com.example.models.Conta;
import com.example.repositories.CartaoRepository;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

@QuarkusTest
public class CartaoServiceTest {

    @Inject
    CartaoService cartaoService;

    @InjectMock
    CartaoRepository cartaoRepository;

    @InjectMock
    ContaService contaService;

    @Test
    void testeCriarCartaoFisico() {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setNome("Titular Teste");

        when(contaService.buscarPorContaId(1L)).thenReturn(Optional.of(conta));
        doNothing().when(cartaoRepository).persist(any(Cartao.class));

        CartaoRequestDTO dto = new CartaoRequestDTO(TipoCartao.FISICO, 1L);
        CartaoResponseDTO response = cartaoService.criarNovoCartao(dto);

        assertNotNull(response);
        assertEquals(TipoCartao.FISICO.toString(), response.getTipo());
    }

    @Test
    void testeAtivarCartao() {
        Cartao cartao = new Cartao();
        cartao.setNumero("1234567890123456");
        cartao.setTipo(TipoCartao.FISICO);
        cartao.setDeliveryStatus(DeliveryStatus.ENTREGUE);

        when(cartaoRepository.buscarCartaoPorNumeroCartao("1234567890123456"))
            .thenReturn(Optional.of(cartao));

        cartaoService.ativarCartao("1234567890123456");
        
        assertEquals(StatusCartao.ATIVO, cartao.getStatus());
    }

    @Test
    void testeBloquearCartao() {
        Cartao cartao = new Cartao();
        cartao.setNumero("1234567890123456");

        when(cartaoRepository.buscarCartaoPorNumeroCartao("1234567890123456"))
            .thenReturn(Optional.of(cartao));

        cartaoService.bloquearCartao("1234567890123456");
        
        assertEquals(StatusCartao.BLOQUEADO, cartao.getStatus());
    }
}
