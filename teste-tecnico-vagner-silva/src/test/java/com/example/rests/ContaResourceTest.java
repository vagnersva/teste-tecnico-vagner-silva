package com.example.rests;

import com.example.dtos.ContaRequestDTO;
import com.example.dtos.ContaResponseDTO;
import com.example.services.ContaService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaResourceTest {

    @Mock
    private ContaService contaService;

    @InjectMocks
    private ContaResource contaResource;

    private ContaRequestDTO contaRequestDTO;
    private ContaResponseDTO contaResponseDTO;

    @BeforeEach
    void setup() {
        contaRequestDTO = new ContaRequestDTO();
        contaRequestDTO.setCpf("12345678900");

        contaResponseDTO = new ContaResponseDTO();
    }

    @Test
    void testeInativarContaPorCpf_Sucesso() {
        when(contaService.inativarConta("12345678900")).thenReturn(contaResponseDTO);

        Response response = contaResource.inativarContaPorCpf("12345678900");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(contaResponseDTO, response.getEntity());
    }

    @Test
    void testeInativarContaPorCpf_ContaNaoEncontrada() {
        when(contaService.inativarConta("12345678900")).thenThrow(new IllegalArgumentException("Conta não encontrada"));

        Response response = contaResource.inativarContaPorCpf("12345678900");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Conta não encontrada", response.getEntity());
    }

    @Test
    void testeCriarNovaConta_Sucesso() {
        when(contaService.criarConta(contaRequestDTO)).thenReturn(contaResponseDTO);

        Response response = contaResource.criarNovaConta(contaRequestDTO);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(contaResponseDTO, response.getEntity());
    }

    @Test
    void testeCriarNovaConta_Erro() {
        when(contaService.criarConta(contaRequestDTO)).thenThrow(new RuntimeException("Erro ao criar conta"));

        Response response = contaResource.criarNovaConta(contaRequestDTO);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Erro ao criar conta"));
    }

    @Test
    void testeBuscarContaPorDocumento_Sucesso() {
        when(contaService.buscarPorDocumento("12345678900")).thenReturn(contaResponseDTO);

        Response response = contaResource.buscarContaPorDocumento("12345678900");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(contaResponseDTO, response.getEntity());
    }

    @Test
    void testeBuscarContaPorDocumento_ContaNaoEncontrada() {
        when(contaService.buscarPorDocumento("12345678900")).thenThrow(new RuntimeException("Conta não encontrada"));

        Response response = contaResource.buscarContaPorDocumento("12345678900");

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Erro ao processar a requisição"));
    }
}
