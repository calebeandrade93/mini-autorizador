package br.com.vr.mini_autorizador.controller;
import br.com.vr.mini_autorizador.dto.TransacaoDTO;
import br.com.vr.mini_autorizador.exceptions.CartaoNaoEncontradoException;
import br.com.vr.mini_autorizador.exceptions.SaldoInsuficienteException;
import br.com.vr.mini_autorizador.exceptions.SenhaDoCartaoInvalidaException;
import br.com.vr.mini_autorizador.service.TransacaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacaoControllerTest {

    @InjectMocks
    private TransacaoController transacaoController;

    @Mock
    private TransacaoService transacaoService;

    @Test
    void realizarTransacao_deveRetornarStatus201EStringOk() {
        TransacaoDTO transacaoDTO = new TransacaoDTO("1234567891234567", "senha1234", new BigDecimal("50.00"));

        doNothing().when(transacaoService).processarTransacao(any(TransacaoDTO.class));

        ResponseEntity<String> response = transacaoController.realizarTransacao(transacaoDTO);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("OK", response.getBody());

        verify(transacaoService, times(1)).processarTransacao(transacaoDTO);
    }

    @Test
    void realizerTransacao_deveRetornarStatus422SaldoInsuficiente() {
        TransacaoDTO transacaoDTO = new TransacaoDTO("1234567891234567", "senha1234", new BigDecimal("150.00"));
        doThrow(new SaldoInsuficienteException("SALDO_INSUFICIENTE"))
                .when(transacaoService).processarTransacao(any(TransacaoDTO.class));

        SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class, () -> {
            transacaoController.realizarTransacao(transacaoDTO);
        });

        assertEquals("SALDO_INSUFICIENTE", exception.getMessage());
        verify(transacaoService, times(1)).processarTransacao(transacaoDTO);
    }

    @Test
    void realizarTransacao_deveRetornarStatus404CartaoNaoEncontrado() {
        TransacaoDTO transacaoDTO = new TransacaoDTO("0000000000000000", "senha1234", new BigDecimal("50.00"));
        doThrow(new CartaoNaoEncontradoException("CARTAO_INEXISTENTE"))
                .when(transacaoService).processarTransacao(any(TransacaoDTO.class));
        CartaoNaoEncontradoException exception = assertThrows(CartaoNaoEncontradoException.class, () -> {
            transacaoController.realizarTransacao(transacaoDTO);
        });

        assertEquals("CARTAO_INEXISTENTE", exception.getMessage());
        verify(transacaoService, times(1)).processarTransacao(transacaoDTO);
    }

    @Test
    void realizarTransacao_deveRetornarStatus422SenhaInvalida() {
        TransacaoDTO transacaoDTO = new TransacaoDTO("1234567891234567", "senhaErrada", new BigDecimal("50.00"));
        doThrow(new SenhaDoCartaoInvalidaException("SENHA_INVALIDA"))
                .when(transacaoService).processarTransacao(any(TransacaoDTO.class));

        SenhaDoCartaoInvalidaException exception = assertThrows(SenhaDoCartaoInvalidaException.class, () -> {
            transacaoController.realizarTransacao(transacaoDTO);
        });

        assertEquals("SENHA_INVALIDA", exception.getMessage());
        verify(transacaoService, times(1)).processarTransacao(transacaoDTO);
    }
}