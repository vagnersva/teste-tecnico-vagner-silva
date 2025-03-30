package com.example.rests;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.example.dtos.RotacaoCvvWebhookDTO;
import com.example.dtos.TransportadoraWebhookDTO;
import com.example.services.WebhookService;

@Path("/webhooks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Webhooks", description = "Endpoints para integração com serviços externos")
public class WebhookResource {

    @Inject
    WebhookService webhookService;

    @Path("/transportadora")
    @POST
    @Operation(
        summary = "Atualização de status de entrega",
        description = "Recebe webhook com atualizações de status de entrega da transportadora"
    )
    public Response receberStatusEntrega(TransportadoraWebhookDTO dto) {
        try {
            webhookService.processarAtualizacaoEntrega(dto);
            return Response.ok().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/cvv-rotativo")
    @POST
    @Operation(
        summary = "Rotação de CVV",
        description = "Processa a rotação periódica do código de segurança do cartão (CVV)"
    )
    public Response atualizarCvv(RotacaoCvvWebhookDTO dto) {
        try {
            webhookService.processarRotacaoCvv(dto);
            return Response.ok().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}



    
