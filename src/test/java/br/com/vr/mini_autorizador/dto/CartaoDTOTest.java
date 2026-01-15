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

    //Configuração do validador para rodar os testes
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
        CartaoDTO dto = new CartaoDTO("1234567890123456", "minhasenha");
        Set<ConstraintViolation<CartaoDTO>> violacoes = validator.validate(dto);
        
        assertTrue(violacoes.isEmpty());
    }

    @Test
    void quandoNumeroCartaoMenorQue16() {
        CartaoDTO dto = new CartaoDTO("123", "senha");
        Set<ConstraintViolation<CartaoDTO>> violacoes = validator.validate(dto);
        
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(violacao ->
                "numeroCartao".equals(violacao.getPropertyPath().toString()) &&
                        violacao.getMessage().contains("O cartão deve conter exatamente 16 dígitos")));
    }

    @Test
    void quandoNumeroCartaoContemLetras() {
        CartaoDTO dto = new CartaoDTO("1234abcd5678efgh", "senha");
        Set<ConstraintViolation<CartaoDTO>> violacoes = validator.validate(dto);

        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(violacao ->
                "numeroCartao".equals(violacao.getPropertyPath().toString()) &&
                        violacao.getMessage().contains("O cartão deve conter apenas números")));
    }

    @Test
    void quandoNumeroCartaoVazio() {
        CartaoDTO dto = new CartaoDTO("", "senha");
        Set<ConstraintViolation<CartaoDTO>> violacoes = validator.validate(dto);

        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(violacao ->
                "numeroCartao".equals(violacao.getPropertyPath().toString())));
    }

    @Test
    void quandoSenhaBlank() {
        var dto = new CartaoDTO("1234567890123456", "");
        Set<ConstraintViolation<CartaoDTO>> violacoes = validator.validate(dto);

        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v ->
                "senha".equals(v.getPropertyPath().toString())));
    }
}
