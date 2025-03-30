package com.example.rests;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.example.dtos.CartaoRequestDTO;
import com.example.dtos.CartaoResponseDTO;
import com.example.dtos.ReemissaoCartaoRequestDTO;
import com.example.services.CartaoService;

@Path("/cartao")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Cartão", description = "Operações relacionadas ao gerenciamento de cartões")
public class CartaoResource {
    @Inject
    CartaoService cartaoService;

    @GET
    @Path("/listar")
    @Operation(
        summary = "Listar cartões",
        description = "Retorna todos os cartões associados a uma conta"
    )
    public Response listarCartoes(@QueryParam("contaId") Long contaId) {
        try {
            List<CartaoResponseDTO> cartoes = cartaoService.listarCartoes(contaId);
            return Response.ok(cartoes).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    
    @POST
    @Path("/cadastrar")
    @Transactional 
    @Operation(
        summary = "Cadastrar novo cartão",
        description = "Cria um novo cartão associado a uma conta existente"
    )
    public Response criarCartao(CartaoRequestDTO dto) {
        try {
            CartaoResponseDTO cartaoCriado = cartaoService.criarNovoCartao(dto);
            return Response.status(Response.Status.CREATED).entity(cartaoCriado).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity("Erro ao criar cartao: " + e.getMessage())
                         .build();
        }
    }

    @PATCH
    @Path("/ativar")
    @Transactional 
    @Operation(
        summary = "Ativar cartão",
        description = "Ativa um cartão previamente inativo"
    )
    public Response ativarCartao(@QueryParam("numero") String numeroCartao) {
        try {
            cartaoService.ativarCartao(numeroCartao);

            return Response.ok().entity("Cartão ativado com sucesso").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity("Erro ao ativar cartao: " + e.getMessage())
                         .build();
        }
    }

    @PATCH
    @Path("/bloquear")
    @Transactional 
    @Operation(
        summary = "Bloquear cartão",
        description = "Bloqueia um cartão ativo por motivos de segurança"
    )
    public Response bloquearCartao(@QueryParam("numero") String numeroCartao) {
        try {
            cartaoService.bloquearCartao(numeroCartao);

            return Response.ok().entity("Cartão bloqueado com sucesso").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity("Erro ao bloquear cartao: " + e.getMessage())
                         .build();
        }
    }

    @POST
    @Path("/reemitir")
    @Transactional 
    @Operation(
        summary = "Reemitir cartão",
        description = "Solicita a reemissão de um cartão perdido ou danificado"
    )
    public Response reemitirCartao(ReemissaoCartaoRequestDTO dto) {
        try {
            CartaoResponseDTO cartaoReemitido = cartaoService.reemitirCartao(dto);
            return Response.status(Response.Status.CREATED).entity(cartaoReemitido).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity("Erro ao criar cartao: " + e.getMessage())
                         .build();
        }
    }


}



    
