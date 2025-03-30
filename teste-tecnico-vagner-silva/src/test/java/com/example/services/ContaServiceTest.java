package com.example.services;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import org.junit.jupiter.api.Test;

import com.example.dtos.ContaRequestDTO;
import com.example.dtos.ContaResponseDTO;
import com.example.dtos.EnderecoRequestDTO;
import com.example.enums.StatusConta;
import com.example.models.Conta;
import com.example.models.Endereco;
import com.example.repositories.ContaRepository;
import com.example.repositories.EnderecoRepository;

import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

@QuarkusTest
public class ContaServiceTest {

    @Inject
    ContaService contaService;

    @InjectMock
    ContaRepository contaRepository;

    @InjectMock
    EnderecoRepository enderecoRepository;

    @Test
    void testeCriarContaSuccess() {
        ContaRequestDTO dto = new ContaRequestDTO();
        dto.setNome("Teste");
        dto.setCpf("11122233344");
        dto.setTelefone("999999999");
        dto.setEmail("teste@example.com");

        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua A");
        endereco.setNumero("123");
        endereco.setCidade("Cidade X");
        endereco.setEstado("Estado Y");
        endereco.setCep("00000-000");

        EnderecoRequestDTO enderecoRequestDTO = new EnderecoRequestDTO(endereco.getLogradouro(), 
            endereco.getNumero(),
            endereco.getCidade(),
            endereco.getEstado(),
            endereco.getCep()
            );

        dto.setEndereco(enderecoRequestDTO);

        when(contaRepository.buscarPorCpf("11122233344")).thenReturn(Optional.empty());
        doNothing().when(contaRepository).persist(any(Conta.class));

        ContaResponseDTO response = contaService.criarConta(dto);
        assertNotNull(response);
        assertEquals("11122233344", response.getCpf());
    }

    @Test
    void testeCriarContaAlreadyExists() {
        ContaRequestDTO dto = new ContaRequestDTO();
        dto.setNome("Teste");
        dto.setCpf("11122233344");
        dto.setTelefone("999999999");
        dto.setEmail("teste@example.com");

        EnderecoRequestDTO enderecoRequestDTO = new EnderecoRequestDTO("Rua A","123","Cidade X","Estado Y","00000-000");

        dto.setEndereco(enderecoRequestDTO);

        Conta contaExistente = new Conta();
        contaExistente.setCpf("11122233344");
        when(contaRepository.buscarPorCpf("11122233344")).thenReturn(Optional.of(contaExistente));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            contaService.criarConta(dto);
        });
        assertTrue(exception.getMessage().contains("CPF já cadastrado"));
    }

    @Test
    void testeBuscarPorDocumentoSuccess() {
        Conta conta = new Conta();
        conta.setCpf("11122233344");
        conta.setNome("Teste");

        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua A");
        conta.setEndereco(endereco);

        when(contaRepository.buscarPorCpf("11122233344")).thenReturn(Optional.of(conta));

        ContaResponseDTO response = contaService.buscarPorDocumento("11122233344");
        assertNotNull(response);
        assertEquals("11122233344", response.getCpf());
    }

    @Test
    void testeBuscarPorDocumentoNotFound() {
        when(contaRepository.buscarPorCpf("11122233344")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            contaService.buscarPorDocumento("11122233344");
        });
    }

    @Test
    void testeInativarContaSuccess() {
        Conta conta = new Conta();
        conta.setCpf("11122233344");
        conta.setStatusConta(StatusConta.ATIVA);

        Endereco endereco = new Endereco();
        conta.setEndereco(endereco);

        @SuppressWarnings("unchecked")
        PanacheQuery<Conta> query = mock(PanacheQuery.class);
        when(contaRepository.find("cpf", "11122233344")).thenReturn(query);
        when(query.firstResult()).thenReturn(conta);
        doNothing().when(contaRepository).persist(any(Conta.class));

        ContaResponseDTO response = contaService.inativarConta("11122233344");
        assertNotNull(response);
        assertEquals(StatusConta.INATIVA, conta.getStatusConta());
    }

    @Test
    void testeInativarContaAlreadyInactive() {
        Conta conta = new Conta();
        conta.setCpf("11122233344");
        conta.setStatusConta(StatusConta.INATIVA);

        Endereco endereco = new Endereco();
        conta.setEndereco(endereco);

        @SuppressWarnings("unchecked")
        PanacheQuery<Conta> query = mock(PanacheQuery.class);
        when(contaRepository.find("cpf", "11122233344")).thenReturn(query);
        when(query.firstResult()).thenReturn(conta);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            contaService.inativarConta("11122233344");
        });
        assertTrue(exception.getMessage().contains("Conta já está inativa"));
    }

    @Test
    void testeInativarContaNotFound() {
        @SuppressWarnings("unchecked")
        PanacheQuery<Conta> query = mock(PanacheQuery.class);
        when(contaRepository.find("cpf", "11122233344")).thenReturn(query);
        when(query.firstResult()).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            contaService.inativarConta("11122233344");
        });
        assertTrue(exception.getMessage().contains("Conta não encontrada para o CPF"));
    }
}
