package br.com.vr.mini_autorizador.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartaoDTO {

    @NotBlank
    @Size(min = 16, max = 16, message = "O cartão deve ter exatamente 16 dígitos")
    @Pattern(regexp = "\\d+", message = "O cartão deve conter apenas números")
    private String numeroCartao;

    @NotBlank
    private String senha;
}
