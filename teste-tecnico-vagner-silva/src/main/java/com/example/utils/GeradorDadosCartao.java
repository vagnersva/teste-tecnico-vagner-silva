package com.example.utils;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GeradorDadosCartao {
    private static final SecureRandom random = new SecureRandom();

    private static final String PREFIXO_TRACKING = "BR"; 

    public static String gerarTrackingId() {
        StringBuilder sb = new StringBuilder(PREFIXO_TRACKING);
        for (int i = 0; i < 9; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static String gerarNumeroCartao() {
        StringBuilder sb = new StringBuilder();
        sb.append("4"); 
        
        for (int i = 0; i < 15; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static String gerarCVV() {
        return String.format("%03d", random.nextInt(1000));
    }

    public static String gerarDataValidade() {
        LocalDate hoje = LocalDate.now();
        LocalDate validade = hoje.plusYears(3);
        return validade.format(DateTimeFormatter.ofPattern("MM/yy"));
    }
}
