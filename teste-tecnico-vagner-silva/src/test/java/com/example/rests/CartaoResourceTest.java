package com.example.rests;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.*;

import com.example.dtos.CartaoRequestDTO;
import com.example.dtos.CartaoResponseDTO;
import com.example.dtos.ReemissaoCartaoRequestDTO;
import com.example.services.CartaoService;

import java.util.Collections;

@QuarkusTest
public class CartaoResourceTest {

    @InjectMock
    CartaoService cartaoService;

    @Test
    void testeListarCartoes_Sucesso() {
        when(cartaoService.listarCartoes(1L)).thenReturn(Collections.emptyList());

        given()
            .queryParam("contaId", 1L)
            .when()
            .get("/cartao/listar")
            .then()
            .statusCode(200);
    }

    @Test
    void testeCriarCartao_Sucesso() {
        CartaoRequestDTO dto = new CartaoRequestDTO();
        CartaoResponseDTO responseDTO = new CartaoResponseDTO();

        when(cartaoService.criarNovoCartao(any(CartaoRequestDTO.class))).thenReturn(responseDTO);

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .when()
            .post("/cartao/cadastrar")
            .then()
            .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    void testeAtivarCartao_Sucesso() {
        doNothing().when(cartaoService).ativarCartao("1234567890123456");

        given()
            .queryParam("numero", "1234567890123456")
            .when()
            .patch("/cartao/ativar")
            .then()
            .statusCode(200);
    }

    @Test
    void testeBloquearCartao_Sucesso() {
        doNothing().when(cartaoService).bloquearCartao("1234567890123456");

        given()
            .queryParam("numero", "1234567890123456")
            .when()
            .patch("/cartao/bloquear")
            .then()
            .statusCode(200);
    }

    @Test
    void testeReemitirCartao_Sucesso() {
        ReemissaoCartaoRequestDTO dto = new ReemissaoCartaoRequestDTO();
        CartaoResponseDTO responseDTO = new CartaoResponseDTO();

        when(cartaoService.reemitirCartao(any(ReemissaoCartaoRequestDTO.class))).thenReturn(responseDTO);

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .when()
            .post("/cartao/reemitir")
            .then()
            .statusCode(Response.Status.CREATED.getStatusCode());
    }
}
