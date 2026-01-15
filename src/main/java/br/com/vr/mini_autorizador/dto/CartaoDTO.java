package br.com.vr.mini_autorizador.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CartaoDTO(

    @NotBlank
    @Size(min = 16, max = 16, message = "O cartão deve conter exatamente 16 dígitos")
    @Pattern(regexp = "\\d+", message = "O cartão deve conter apenas números")
    String numeroCartao,

    @NotBlank
    String senha
){}
