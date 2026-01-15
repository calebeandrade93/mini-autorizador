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

public class TransacaoDTOTest {

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
    void quandoValido() {
        TransacaoDTO dto = new TransacaoDTO("1234567890123456", "minhasenha", new BigDecimal("10.00"));
        Set<ConstraintViolation<TransacaoDTO>> violacoes = validator.validate(dto);
        
        assertTrue(violacoes.isEmpty());
    }

    @Test
    void quandoNumeroCartaoMenorQue16() {
        TransacaoDTO dto = new TransacaoDTO("123", "senha", new BigDecimal("10.00"));
        Set<ConstraintViolation<TransacaoDTO>> violacoes = validator.validate(dto);

        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(violacao ->
                "numeroCartao".equals(violacao.getPropertyPath().toString()) &&
                        violacao.getMessage().contains("O cartão deve conter exatamente 16 dígitos")
        ));
    }

    @Test
    void quandoNumeroCartaoContemLetras() {
        TransacaoDTO dto = new TransacaoDTO("1234abcd5678efgh", "senha", new BigDecimal("10.00"));
        Set<ConstraintViolation<TransacaoDTO>> violacoes = validator.validate(dto);

        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(violacao ->
                "numeroCartao".equals(violacao.getPropertyPath().toString()) &&
                        violacao.getMessage().contains("O cartão deve conter apenas números")
        ));
    }

    @Test
    void quandoNumeroCartaoBlank() {
        TransacaoDTO dto = new TransacaoDTO("", "senha", new BigDecimal("10.00"));
        Set<ConstraintViolation<TransacaoDTO>> violacoes = validator.validate(dto);

        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(violacao ->
                "numeroCartao".equals(violacao.getPropertyPath().toString())
        ));
    }

    @Test
    void quandoSenhaBlank() {
        TransacaoDTO dto = new TransacaoDTO("1234567890123456", "", new BigDecimal("10.00"));
        Set<ConstraintViolation<TransacaoDTO>> violacoes = validator.validate(dto);

        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(violacao ->
                "senhaCartao".equals(violacao.getPropertyPath().toString())
        ));
    }

    @Test
    void quandoValorNull() {
        TransacaoDTO dto = new TransacaoDTO("1234567890123456", "senha", null);
        Set<ConstraintViolation<TransacaoDTO>> violacoes = validator.validate(dto);

        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(violacao ->
                "valor".equals(violacao.getPropertyPath().toString())
        ));
    }

    @Test
    void quandoValorNegativo() {
        TransacaoDTO dtoNeg = new TransacaoDTO("1234567890123456", "senha", new BigDecimal("-1"));
        Set<ConstraintViolation<TransacaoDTO>> violacoesNeg = validator.validate(dtoNeg);

        assertFalse(violacoesNeg.isEmpty());
        assertTrue(violacoesNeg.stream().anyMatch(violacao ->
                "valor".equals(violacao.getPropertyPath().toString())
        ));
    }

    @Test
    void quandoValorZero() {
        TransacaoDTO dto = new TransacaoDTO("1234567890123456", "senha", BigDecimal.ZERO);
        Set<ConstraintViolation<TransacaoDTO>> violacoes = validator.validate(dto);

        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(violacao ->
                "valor".equals(violacao.getPropertyPath().toString())
        ));
    }
}
