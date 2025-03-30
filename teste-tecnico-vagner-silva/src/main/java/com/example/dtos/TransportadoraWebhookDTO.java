package com.example.dtos;

import java.time.LocalDateTime;

import com.example.enums.DeliveryStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TransportadoraWebhookDTO {
    @NotBlank
    @Size(min = 16, max = 16, message = "Número do cartão deve ter 16 dígitos")
    @Pattern(regexp = "\\d+", message = "Número do cartão deve conter apenas dígitos")
    private String numeroCartao;

    @NotBlank(message = "Tracking ID é obrigatório")
    private String trackingId;

    @NotNull(message = "Status de entrega é obrigatório")
    private DeliveryStatus deliveryStatus;

    @FutureOrPresent(message = "Data de entrega não pode ser no passado")
    private LocalDateTime deliveryDate;


    private String deliveryReturnReason;

    @Valid
    @NotNull(message = "Endereço de entrega é obrigatório")
    private EnderecoRequestDTO deliveryAddress;

    public String getNumeroCartao() {
        return this.numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getTrackingId() {
        return trackingId;
    }
    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }
    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }
    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }
    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    public String getDeliveryReturnReason() {
        return deliveryReturnReason;
    }
    public void setDeliveryReturnReason(String deliveryReturnReason) {
        this.deliveryReturnReason = deliveryReturnReason;
    }
    public EnderecoRequestDTO getDeliveryAddress() {
        return deliveryAddress;
    }
    public void setDeliveryAddress(EnderecoRequestDTO deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
