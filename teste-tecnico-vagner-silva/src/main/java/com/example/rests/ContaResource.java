package com.example.rests;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.example.dtos.ContaRequestDTO;
import com.example.dtos.ContaResponseDTO;
import com.example.services.ContaService;

@Path("/conta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Conta", description = "Operações relacionadas ao gerenciamento de contas")
public class ContaResource {
    @Inject
    ContaService contaService;

    @PATCH
    @Path("/inativar")
    @Transactional 
    @Operation(
        summary = "Inativar conta",
        description = "Inativa uma conta existente com base no CPF"
    )
    public Response inativarContaPorCpf(@QueryParam("cpf") String cpf) {
        try {
            ContaResponseDTO contaInativada = contaService.inativarConta(cpf);
            return Response.ok(contaInativada).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                         .entity(e.getMessage())
                         .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                         .entity("Erro ao inativar conta: " + e.getMessage())
                         .build();
        }
    }

    @POST
    @Path("/cadastrar")
    @Transactional 
    @Operation(
        summary = "Criar nova conta",
        description = "Cadastra uma nova conta bancária"
    )
    public Response criarNovaConta(ContaRequestDTO dto) {
        try {
            ContaResponseDTO contaCriada = contaService.criarConta(dto);
            return Response.status(Response.Status.CREATED).entity(contaCriada).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity("Erro ao criar conta: " + e.getMessage())
                         .build();
        }
    }

    @GET
    @Path("/buscar")
    @Operation(
        summary = "Buscar conta por documento",
        description = "Recupera informações da conta usando o CPF do titular"
    )
    public Response buscarContaPorDocumento(@QueryParam("cpf") String cpf) {
        try {
            ContaResponseDTO conta = contaService.buscarPorDocumento(cpf);
            
            if (conta == null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Nenhuma conta encontrada para o documento: " + cpf)
                               .build();
            }

            return Response.status(Response.Status.OK).entity(conta).build();


        } catch (Exception e) {
            return Response.serverError()
                           .entity("Erro ao processar a requisição: " + e.getMessage())
                           .build();
        }
    }

}



    
