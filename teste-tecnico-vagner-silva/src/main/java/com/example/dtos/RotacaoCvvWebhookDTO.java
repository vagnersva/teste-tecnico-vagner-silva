package com.example.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class RotacaoCvvWebhookDTO {
    @NotBlank(message = "Account ID é obrigatório")
    private Long accountId;
    
    @NotBlank(message = "Card ID é obrigatório")
    private Long cardId;
    
    @NotNull(message = "Novo CVV é obrigatório")
    @Pattern(regexp = "\\d{3}", message = "CVV deve conter 3 dígitos")
    private Integer nextCvv;
    
    @NotNull(message = "Data de expiração é obrigatória")
    private LocalDateTime expirationDate;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Integer getNextCvv() {
        return nextCvv;
    }

    public void setNextCvv(Integer nextCvv) {
        this.nextCvv = nextCvv;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }    
}
