package com.example.services;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import com.example.dtos.ReemissaoCartaoRequestDTO;
import com.example.enums.MotivoReemissao;
import com.example.models.Cartao;
import com.example.models.ReemissaoCartao;
import com.example.repositories.ReemissaoCartaoRepository;
import jakarta.inject.Inject;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
public class ReemissaoServiceTest {

    @Inject
    ReemissaoService reemissaoService;

    @InjectMock
    ReemissaoCartaoRepository repository;

    void testeSalvarReemissaoCartaoSuccess() {
        Cartao cartaoBloqueado = new Cartao();
        cartaoBloqueado.setNumero("1234567890123456");

        ReemissaoCartaoRequestDTO dto = new ReemissaoCartaoRequestDTO();
        dto.setMotivo(MotivoReemissao.PERDA);

        doNothing().when(repository).persist(any(ReemissaoCartao.class));

        assertDoesNotThrow(() -> reemissaoService.salvarReemissaoCartao(cartaoBloqueado, dto));

        verify(repository, times(1)).persist(any(ReemissaoCartao.class));
    }
}
