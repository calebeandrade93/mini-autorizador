package br.com.vr.mini_autorizador.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TransacaoDTO(

        @NotBlank
        @Size(min = 16, max = 16, message = "O cartão deve conter exatamente 16 dígitos")
        @Pattern(regexp = "\\d+", message = "O cartão deve conter apenas números")
        String numeroCartao,

        @NotBlank
        String senhaCartao,

        @NotNull
        @Positive
        BigDecimal valor
) {}