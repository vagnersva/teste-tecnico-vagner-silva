package com.example.services;

import java.util.Optional;

import com.example.dtos.ContaRequestDTO;
import com.example.dtos.ContaResponseDTO;
import com.example.enums.DeliveryStatus;
import com.example.enums.StatusCartao;
import com.example.enums.StatusConta;
import com.example.enums.TipoCartao;
import com.example.models.Cartao;
import com.example.models.Conta;
import com.example.models.Endereco;
import com.example.repositories.ContaRepository;
import com.example.repositories.EnderecoRepository;
import com.example.utils.GeradorDadosCartao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class ContaService {
    @Inject
    ContaRepository contaRepository;

    @Inject
    EnderecoRepository enderecoRepository;

    public ContaResponseDTO buscarPorDocumento(String cpf) {
        return contaRepository.buscarPorCpf(cpf)
                .map(conta -> new ContaResponseDTO(conta, conta.getEndereco())) 
                .orElseThrow(() -> new NotFoundException("Conta não encontrada para o documento: " + cpf));
    }

    @Transactional
    public ContaResponseDTO criarConta(ContaRequestDTO dto) {
        if (verificarSeContaExiste(dto.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado"); 
        }
    
        Conta conta = new Conta();
        conta.setStatusConta(StatusConta.ATIVA);
        conta.setNome(dto.getNome());
        conta.setCpf(dto.getCpf());
        conta.setTelefone(dto.getTelefone());
        conta.setEmail(dto.getEmail());
    
        Endereco endereco = new Endereco();
        endereco.setLogradouro(dto.getEndereco().getLogradouro());
        endereco.setNumero(dto.getEndereco().getNumero());
        endereco.setCidade(dto.getEndereco().getCidade());
        endereco.setEstado(dto.getEndereco().getEstado());
        endereco.setCep(dto.getEndereco().getCep());
        endereco.setConta(conta);
        
        Cartao cartaoFisico = new Cartao();
        cartaoFisico.setNumero(GeradorDadosCartao.gerarNumeroCartao()); 
        cartaoFisico.setTitular(conta.getNome());  
        cartaoFisico.setCvv(GeradorDadosCartao.gerarCVV());  
        cartaoFisico.setDataValidade(GeradorDadosCartao.gerarDataValidade()); 
        cartaoFisico.setTipo(TipoCartao.FISICO);
        cartaoFisico.setStatus(StatusCartao.INATIVO); 
        cartaoFisico.setTrackingId(GeradorDadosCartao.gerarTrackingId());
        cartaoFisico.setDeliveryStatus(DeliveryStatus.PROCESSANDO);
        cartaoFisico.setDeliveryAddress(endereco);
        cartaoFisico.setConta(conta);
        
        conta.getCartoes().add(cartaoFisico);
        conta.setEndereco(endereco);

        contaRepository.persist(conta);

        return new ContaResponseDTO(conta, endereco);
    }

    boolean verificarSeContaExiste(String cpf) {
        Optional<Conta> conta = contaRepository.buscarPorCpf(cpf);
        if (conta.isPresent()) {
            return true;
        }
        return false;
    }

    @Transactional
    public ContaResponseDTO inativarConta(String cpf) {
        Conta conta = contaRepository.find("cpf", cpf).firstResult();
        
        if (conta == null) {
            throw new IllegalArgumentException("Conta não encontrada para o CPF: " + cpf);
        }
    
        if (conta.getStatusConta() == StatusConta.INATIVA) {
            throw new IllegalArgumentException("Conta já está inativa");
        }
    
        conta.setStatusConta(StatusConta.INATIVA);
        contaRepository.persist(conta); 
    
        return new ContaResponseDTO(conta, conta.getEndereco());
    }

    public Optional<Conta> buscarPorContaId(Long contaId) {        
        return contaRepository.findByIdOptional(contaId);
    }

    public void atualizarConta(Conta conta) {
        contaRepository.persist(conta);
    }
}