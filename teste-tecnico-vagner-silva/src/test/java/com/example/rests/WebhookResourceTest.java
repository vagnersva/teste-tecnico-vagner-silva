package com.example.rests;

import com.example.dtos.EnderecoRequestDTO;
import com.example.dtos.RotacaoCvvWebhookDTO;
import com.example.dtos.TransportadoraWebhookDTO;
import com.example.enums.DeliveryStatus;
import com.example.enums.TipoCartao;
import com.example.models.Cartao;
import com.example.models.Conta;
import com.example.repositories.ReemissaoCartaoRepository;
import com.example.services.CartaoService;
import com.example.services.ContaService;
import com.example.services.WebhookService;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebhookResourceTest {

    @Mock
    private ReemissaoCartaoRepository repository;

    @Mock
    private CartaoService cartaoService;

    @Mock
    private ContaService contaService;

    @InjectMocks
    private WebhookService webhookService;

    private Cartao cartaoFisico;
    private Cartao cartaoVirtual;
    private TransportadoraWebhookDTO transportadoraWebhookDTO;
    private RotacaoCvvWebhookDTO rotacaoCvvWebhookDTO;
    private Conta conta;

    @BeforeEach
    void setup() {
        conta = new Conta();
        cartaoFisico = new Cartao();
        cartaoFisico.setTipo(TipoCartao.FISICO);
        cartaoFisico.setConta(conta);

        cartaoVirtual = new Cartao();
        cartaoVirtual.setTipo(TipoCartao.VIRTUAL);

        transportadoraWebhookDTO = new TransportadoraWebhookDTO();
        transportadoraWebhookDTO.setNumeroCartao("1234567890123456");
        transportadoraWebhookDTO.setDeliveryStatus(DeliveryStatus.ENTREGUE);
        transportadoraWebhookDTO.setDeliveryDate(LocalDateTime.now());
        transportadoraWebhookDTO.setDeliveryReturnReason(null);
        transportadoraWebhookDTO.setDeliveryAddress(new EnderecoRequestDTO("Rua A", "123", "Cidade X", "Estado Y", "12345-678"));

        rotacaoCvvWebhookDTO = new RotacaoCvvWebhookDTO();
        rotacaoCvvWebhookDTO.setCardId(1L);
        rotacaoCvvWebhookDTO.setAccountId(1L);
        rotacaoCvvWebhookDTO.setNextCvv(123);
        rotacaoCvvWebhookDTO.setExpirationDate(LocalDateTime.now().plusDays(1));
    }

    @Test
    void testeProcessarAtualizacaoEntrega_CartaoNaoEncontrado() {
        when(cartaoService.buscarPorNumeroCartao(anyString())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            webhookService.processarAtualizacaoEntrega(transportadoraWebhookDTO);
        });

        assertEquals("Cartão não encontrado.", exception.getMessage());
    }

    @Test
    void testeProcessarAtualizacaoEntrega_CartaoNaoFisico() {
        when(cartaoService.buscarPorNumeroCartao(anyString())).thenReturn(Optional.of(cartaoVirtual));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            webhookService.processarAtualizacaoEntrega(transportadoraWebhookDTO);
        });

        assertEquals("Apenas cartões físicos podem ter sua entrega confirmada.", exception.getMessage());
    }

    @Test
    void testeProcessarAtualizacaoEntrega_Sucesso() {
        when(cartaoService.buscarPorNumeroCartao(anyString())).thenReturn(Optional.of(cartaoFisico));

        webhookService.processarAtualizacaoEntrega(transportadoraWebhookDTO);

        assertEquals(DeliveryStatus.ENTREGUE, cartaoFisico.getDeliveryStatus());
        assertNotNull(cartaoFisico.getDeliveryDate());
        verify(cartaoService, times(1)).atualizarDadosCartao(cartaoFisico);
    }

    @Test
    void testeProcessarRotacaoCvv_CartaoNaoEncontrado() {
        when(cartaoService.buscarPorCartaoId(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            webhookService.processarRotacaoCvv(rotacaoCvvWebhookDTO);
        });

        assertEquals("Cartão não encontrado", exception.getMessage());
    }

    @Test
    void testeProcessarRotacaoCvv_DataExpirada() {
        rotacaoCvvWebhookDTO.setExpirationDate(LocalDateTime.now().minusDays(1));
        when(cartaoService.buscarPorCartaoId(anyLong())).thenReturn(Optional.of(cartaoFisico));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            webhookService.processarRotacaoCvv(rotacaoCvvWebhookDTO);
        });

        assertEquals("Data de expiração do CVV inválida.", exception.getMessage());
    }

    @Test
    void testeProcessarRotacaoCvv_CartaoNaoPertenceConta() {
        when(cartaoService.buscarPorCartaoId(anyLong())).thenReturn(Optional.of(cartaoFisico));
        when(contaService.buscarPorContaId(anyLong())).thenReturn(Optional.of(conta));

        conta.setCartoes(List.of());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            webhookService.processarRotacaoCvv(rotacaoCvvWebhookDTO);
        });

        assertEquals("Cartão não encontrado.", exception.getMessage());
    }

    @Test
    void testeProcessarRotacaoCvv_Sucesso() {
        conta.setId(1L);
        cartaoFisico.setId(1L);
        conta.setCartoes(List.of(cartaoFisico));

        rotacaoCvvWebhookDTO.setCardId(1L);
        rotacaoCvvWebhookDTO.setAccountId(1L);

        when(cartaoService.buscarPorCartaoId(eq(1L))).thenReturn(Optional.of(cartaoFisico));
        when(contaService.buscarPorContaId(eq(1L))).thenReturn(Optional.of(conta));

        webhookService.processarRotacaoCvv(rotacaoCvvWebhookDTO);

        // Verificações
        assertEquals("123", cartaoFisico.getCvv()); 
        assertNotNull(cartaoFisico.getDataAtualizacao()); 
        verify(cartaoService, times(1)).atualizarDadosCartao(cartaoFisico); 
    }

}
