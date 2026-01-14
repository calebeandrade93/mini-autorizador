package br.com.vr.mini_autorizador.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TransacaoDTOTest {

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
        var dto = new TransacaoDTO("1234567890123456", "minhasenha", new BigDecimal("100.00"));
        Set<ConstraintViolation<TransacaoDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Não deve haver violações para DTO válido");
    }

    @Test
    void quandoNumeroCartaoMenorQue16_deveConterViolacaoDeTamanho() {
        var dto = new TransacaoDTO("123", "senha", new BigDecimal("10"));
        Set<ConstraintViolation<TransacaoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
                "numeroCartao".equals(v.getPropertyPath().toString()) &&
                        v.getMessage().contains("O cartão deve conter exatamente 16 dígitos")
        ));
    }

    @Test
    void quandoNumeroCartaoContemLetras_deveConterViolacaoDeFormato() {
        var dto = new TransacaoDTO("1234abcd5678efgh", "senha", new BigDecimal("10"));
        Set<ConstraintViolation<TransacaoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
                "numeroCartao".equals(v.getPropertyPath().toString()) &&
                        v.getMessage().contains("O cartão deve conter apenas números")
        ));
    }

    @Test
    void quandoNumeroCartaoBlank_deveConterViolacaoNotBlank() {
        var dto = new TransacaoDTO("", "senha", new BigDecimal("10"));
        Set<ConstraintViolation<TransacaoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
                "numeroCartao".equals(v.getPropertyPath().toString())
        ));
    }

    @Test
    void quandoSenhaBlank_deveConterViolacaoNotBlank() {
        var dto = new TransacaoDTO("1234567890123456", "", new BigDecimal("10"));
        Set<ConstraintViolation<TransacaoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
                "senhaCartao".equals(v.getPropertyPath().toString())
        ));
    }

    @Test
    void quandoValorNull_deveConterViolacaoNotNull() {
        var dto = new TransacaoDTO("1234567890123456", "senha", null);
        Set<ConstraintViolation<TransacaoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
                "valor".equals(v.getPropertyPath().toString())
        ));
    }

    @Test
    void quandoValorNaoPositivo_deveConterViolacaoPositive() {
        var dtoZero = new TransacaoDTO("1234567890123456", "senha", BigDecimal.ZERO);
        var dtoNeg = new TransacaoDTO("1234567890123456", "senha", new BigDecimal("-1"));
        Set<ConstraintViolation<TransacaoDTO>> violationsZero = validator.validate(dtoZero);
        Set<ConstraintViolation<TransacaoDTO>> violationsNeg = validator.validate(dtoNeg);

        assertFalse(violationsZero.isEmpty());
        assertTrue(violationsZero.stream().anyMatch(v -> "valor".equals(v.getPropertyPath().toString())));

        assertFalse(violationsNeg.isEmpty());
        assertTrue(violationsNeg.stream().anyMatch(v -> "valor".equals(v.getPropertyPath().toString())));
    }
}
