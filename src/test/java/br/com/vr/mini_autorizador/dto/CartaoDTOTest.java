package br.com.vr.mini_autorizador.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CartaoDTOTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        factory.close();
    }

    @Test
    void quandoValido_naoDeveConterViolacoes() {
        var dto = new CartaoDTO("1234567890123456", "minhasenha");
        Set<ConstraintViolation<CartaoDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Não deve haver violações para DTO válido");
    }

    @Test
    void quandoNumeroCartaoMenorQue16_deveConterViolacaoDeTamanho() {
        var dto = new CartaoDTO("123", "senha");
        Set<ConstraintViolation<CartaoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
                "numeroCartao".equals(v.getPropertyPath().toString()) &&
                        v.getMessage().contains("O cartão deve conter exatamente 16 dígitos")
        ), "Deve conter mensagem de tamanho para numeroCartao");
    }

    @Test
    void quandoNumeroCartaoContemLetras_deveConterViolacaoDeFormato() {
        var dto = new CartaoDTO("1234abcd5678efgh", "senha");
        Set<ConstraintViolation<CartaoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
                "numeroCartao".equals(v.getPropertyPath().toString()) &&
                        v.getMessage().contains("O cartão deve conter apenas números")
        ), "Deve conter mensagem de formato para numeroCartao");
    }

    @Test
    void quandoNumeroCartaoBlank_deveConterViolacaoNotBlank() {
        var dto = new CartaoDTO("", "senha");
        Set<ConstraintViolation<CartaoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
                "numeroCartao".equals(v.getPropertyPath().toString())
        ), "Deve haver violação em numeroCartao");
    }

    @Test
    void quandoSenhaBlank_deveConterViolacaoNotBlank() {
        var dto = new CartaoDTO("1234567890123456", "");
        Set<ConstraintViolation<CartaoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
                "senha".equals(v.getPropertyPath().toString())
        ), "Deve haver violação em senha");
    }
}
