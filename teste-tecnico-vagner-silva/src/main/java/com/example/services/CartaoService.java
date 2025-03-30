package com.example.services;

import java.util.List;
import java.util.Optional;

import com.example.dtos.CartaoRequestDTO;
import com.example.dtos.CartaoResponseDTO;
import com.example.dtos.ReemissaoCartaoRequestDTO;
import com.example.enums.DeliveryStatus;
import com.example.enums.StatusCartao;
import com.example.enums.TipoCartao;
import com.example.models.Cartao;
import com.example.models.Conta;
import com.example.repositories.CartaoRepository;
import com.example.utils.GeradorDadosCartao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CartaoService {
    @Inject
    CartaoRepository cartaoRepository;

    @Inject 
    ContaService contaService;

    @Inject
    ReemissaoService reemissaoService;

    @Transactional
    public CartaoResponseDTO criarNovoCartao(CartaoRequestDTO dto) {
        
        Optional<Conta> contaOptional = contaService.buscarPorContaId(dto.getContaId());

        if (!contaOptional.isPresent()) {
            throw new IllegalStateException("Conta não encontrada.");
        }

        Conta conta = contaOptional.get();

        validarCriacaoDeCartao(conta, dto.getTipo());
            
        Cartao cartao = new Cartao();
        cartao.setNumero(GeradorDadosCartao.gerarNumeroCartao()); 
        cartao.setTitular(conta.getNome()); 
        cartao.setCvv(GeradorDadosCartao.gerarCVV()); 
        cartao.setDataValidade(GeradorDadosCartao.gerarDataValidade());
        cartao.setTipo(dto.getTipo());
        cartao.setStatus(StatusCartao.INATIVO);
        cartao.setConta(conta);
        cartao.setDeliveryStatus(DeliveryStatus.PROCESSANDO);
        cartao.setDeliveryAddress(conta.getEndereco());
        
        conta.getCartoes().add(cartao);

        contaService.atualizarConta(conta);

        return CartaoResponseDTO.fromEntity(cartao);
    }

    public void validarCriacaoDeCartao(Conta conta, TipoCartao tipo) {      
        
        if (tipo == TipoCartao.VIRTUAL) {
            boolean cartaoFisicoAtivo = conta.getCartoes()
                .stream()
                .anyMatch(cartao -> cartao.getTipo() == TipoCartao.FISICO 
                    && cartao.getStatus() == StatusCartao.ATIVO 
                    && cartao.getDeliveryStatus() == DeliveryStatus.ENTREGUE);
            
            if (!cartaoFisicoAtivo) {
                throw new IllegalStateException("Cartão físico não está ativo.");
            }
        }
    }

    public void ativarCartao(String numeroCartao) {                
        Optional<Cartao> cartaoOptional = cartaoRepository.buscarCartaoPorNumeroCartao(numeroCartao);

        if (!cartaoOptional.isPresent()) {
            throw new NotFoundException("Cartão não encontrado");
        }

        Cartao cartao = cartaoOptional.get();

        if (cartao.getDeliveryStatus() != DeliveryStatus.ENTREGUE) {
            throw new BadRequestException("Cartão físico não entregue. Ativação bloqueada");
        }

        if (cartao.getTipo() != TipoCartao.FISICO) {
            throw new BadRequestException("Apenas cartões físicos podem ser ativados");
        }

        cartao.setStatus(StatusCartao.ATIVO);
        cartaoRepository.persist(cartao);
    }

    public void bloquearCartao(String numeroCartao) {
        Optional<Cartao> cartaoOptional = cartaoRepository.buscarCartaoPorNumeroCartao(numeroCartao);

        if (!cartaoOptional.isPresent()) {
            throw new NotFoundException("Cartão não encontrado");
        }

        Cartao cartao = cartaoOptional.get();


        cartao.setStatus(StatusCartao.BLOQUEADO);
        cartaoRepository.persist(cartao);
    }

    public List<CartaoResponseDTO> listarCartoes(Long contaId) {
        Optional<Conta> contaOptional = contaService.buscarPorContaId(contaId);

        if (!contaOptional.isPresent()) {
            throw new NotFoundException("Conta não encontrada");
        }

        List<Cartao> cartoes = contaOptional.get().getCartoes();

        return cartoes.stream().map(cartao -> CartaoResponseDTO.fromEntity(cartao)).toList();
    }

    public CartaoResponseDTO reemitirCartao(ReemissaoCartaoRequestDTO dto) {
        Optional<Cartao> cartaoOptional = cartaoRepository.buscarCartaoPorNumeroCartao(dto.getNumero());

        if (!cartaoOptional.isPresent()) {
            throw new NotFoundException("Cartão não encontrado");
        }

        Cartao cartaoBloqueado = cartaoOptional.get();

        if (cartaoBloqueado.getTipo() != TipoCartao.FISICO) {
            throw new BadRequestException("Apenas cartões físicos podem ser reemitidos");
        }

        cartaoBloqueado.setStatus(StatusCartao.BLOQUEADO);
        cartaoRepository.persist(cartaoBloqueado);

        reemissaoService.salvarReemissaoCartao(cartaoBloqueado, dto);

        Conta conta = cartaoBloqueado.getConta();
        CartaoRequestDTO novoCartaoRequestDTO = new CartaoRequestDTO(TipoCartao.FISICO, conta.getId());
        
        return this.criarNovoCartao(novoCartaoRequestDTO);       
    }

    public Optional<Cartao> buscarPorNumeroCartao(String numeroCartao) {  
        return cartaoRepository.buscarCartaoPorNumeroCartao(numeroCartao);
    }

    public Optional<Cartao> buscarPorCartaoId(Long cardId) {  
        return cartaoRepository.findByIdOptional(cardId);
    }

    public void atualizarDadosCartao(Cartao cartao) {        
        cartaoRepository.persist(cartao);
    }
}